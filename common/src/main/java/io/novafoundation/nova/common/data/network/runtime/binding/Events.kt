package io.novafoundation.nova.common.data.network.runtime.binding

import android.util.Log
import com.google.gson.Gson
import io.emeraldpay.polkaj.scale.ScaleCodecReader
import io.novafoundation.nova.common.utils.byteToInt
import io.novafoundation.nova.common.utils.event
import io.novafoundation.nova.common.utils.system
import jp.co.soramitsu.fearless_utils.extensions.fromHex
import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.composite.DictEnum
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.composite.Struct
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.fromHex
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.generics.GenericEvent
import jp.co.soramitsu.fearless_utils.runtime.metadata.moduleOrNull
import jp.co.soramitsu.fearless_utils.runtime.metadata.storage
import java.math.BigInteger

class EventRecord(val phase: Phase, val event: GenericEvent.Instance)

sealed class Phase {

    class ApplyExtrinsic(val extrinsicId: BigInteger) : Phase()

    object Finalization : Phase()

    object Initialization : Phase()
}

@HelperBinding
fun bindEventRecord(dynamicInstance: Any?): EventRecord {
    requireType<Struct.Instance>(dynamicInstance)

    val phaseDynamic = dynamicInstance.getTyped<DictEnum.Entry<*>>("phase")

    val phase = when (phaseDynamic.name) {
        "ApplyExtrinsic" -> Phase.ApplyExtrinsic(phaseDynamic.value.cast())
        "Finalization" -> Phase.Finalization
        "Initialization" -> Phase.Initialization
        else -> incompatible()
    }
    val dynamicEvent = dynamicInstance.getTyped<GenericEvent.Instance>("event")

    return EventRecord(phase, dynamicEvent)
}

@UseCaseBinding
fun bindEventRecords(
    scale: String,
    runtime: RuntimeSnapshot,
): List<EventRecord> {
    val returnType = runtime.metadata.system().storage("Events").type.value ?: incompatible()
    val dynamicInstance = returnType.fromHex(runtime, scale)
    requireType<List<*>>(dynamicInstance)
    return dynamicInstance.mapNotNull { dynamicEventRecord ->
        bindOrNull { bindEventRecord(dynamicEventRecord) }
    }
}

@UseCaseBinding
fun bindEventRecordsForFrequency(
    scale: String,
    runtime: RuntimeSnapshot,
): Result<Pair<String,jp.co.soramitsu.fearless_utils.runtime.metadata.module.Error>> {
    return runCatching {
        val returnType = runtime.metadata.system().storage("Events").type.value ?: incompatible()
        val dynamicInstance = returnType.fromHex(runtime, scale)
        requireType<List<*>>(dynamicInstance)
        val dynamicEvents = dynamicInstance.map { it as Struct.Instance }
        val dictEnumEntryEvents = dynamicEvents.map { it.get<DictEnum.Entry<*>>("event") }
        val eventSystems = dictEnumEntryEvents.filter { it?.name == "System" }
        val systemExtrinsics = eventSystems.map { it?.value as DictEnum.Entry<*> }
        val extrinsicStatus = systemExtrinsics.map { it.value as Struct.Instance }
        val dispatchError = extrinsicStatus.filter { it.mapping.keys.contains("dispatch_error") }.map { it.mapping["dispatch_error"] as DictEnum.Entry<*> }
        val errorBody = dispatchError.map { it.value as Struct.Instance }.first()
        val module = runtime.metadata.moduleOrNull((errorBody.mapping["index"] as BigInteger).toInt())
        val moduleErrors = module?.errors?.map { Pair(it.key,it.value) }.orEmpty()
        val error = moduleErrors[(errorBody.mapping["error"] as ByteArray).byteToInt()]
        error
    }
}
