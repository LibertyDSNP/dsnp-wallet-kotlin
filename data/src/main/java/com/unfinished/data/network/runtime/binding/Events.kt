package com.unfinished.data.network.runtime.binding

import com.unfinished.data.network.runtime.model.event.*
import com.unfinished.data.util.byteToInt
import com.unfinished.data.util.system
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
): Result<List<EventType>> {
    return runCatching {
        val returnType = runtime.metadata.system().storage("Events").type.value ?: incompatible()
        val dynamicInstance = returnType.fromHex(runtime, scale)
        requireType<List<*>>(dynamicInstance)
        val dynamicEvents = dynamicInstance.map { it as Struct.Instance }
        val eventTypes = dynamicEvents.map { it.toEventType(runtime) }
        eventTypes
    }
}

fun Struct.Instance.toEventType(runtime: RuntimeSnapshot): EventType {
    val event = mapping["event"] as DictEnum.Entry<*>
    return when (event.name) {
        EventTypes.System.name -> {
            val structInstance = (event.value as? DictEnum.Entry<*>)?.value as? Struct.Instance
            val disptachInfo = structInstance?.mapping?.get("dispatch_info") as? Struct.Instance
            val disptachInfoWeight = disptachInfo?.mapping?.get("weight") as? Struct.Instance
            val disptachInfoClass = disptachInfo?.mapping?.get("class") as? DictEnum.Entry<*>
            val disptachInfoPaysFee = disptachInfo?.mapping?.get("paysFee") as? DictEnum.Entry<*>
            //Checking if dispatch_eror exists
            val disptachError = structInstance?.mapping?.get("dispatch_error") as? DictEnum.Entry<*>
            val disptachErrorValue = disptachError?.value as? Struct.Instance
            var mDisptachError: DisptachError? = null
            disptachErrorValue?.let {
                val module = runtime.metadata.moduleOrNull((disptachErrorValue.mapping["index"] as BigInteger).toInt())
                val moduleErrors = module?.errors?.map { Pair(it.key, it.value) }.orEmpty()
                val error = if (moduleErrors.isNotEmpty())
                     moduleErrors[(disptachErrorValue.mapping["error"] as ByteArray).byteToInt()]
                else null
                mDisptachError = DisptachError(
                    moduleIndex = disptachErrorValue.mapping["index"] as? BigInteger,
                    error = error
                )
            }
            EventType.System(
                name = event.name,
                value = Extrinsic(
                    name = disptachInfo?.mapping?.keys?.first(),
                    value = DisptachInfo(
                        weight = DispatchInfoWeight(
                            refTime = disptachInfoWeight?.get("refTime") as? BigInteger,
                            proofSize = disptachInfoWeight?.get("proofSize") as? BigInteger
                        ),
                        classs = DispatchInfoClass(
                            name = disptachInfoClass?.name,
                            value = (disptachInfoClass?.value as? DictEnum.Entry<*>)?.name
                        ),
                        paysFee = DispatchInfoPaysFee(
                            name = disptachInfoPaysFee?.name,
                            value = (disptachInfoPaysFee?.value as? DictEnum.Entry<*>)?.name
                        )
                    ),
                    error = mDisptachError
                )
            )
        }
        EventTypes.Balance.name -> {
            val withdraw = (event.value as? DictEnum.Entry<*>)?.value as? Struct.Instance
            //val withdraw = structInstance?.mapping?.get("Withdraw") as? Struct.Instance
            EventType.Balance(
                name = event.name,
                value = Withdraw(
                    name = withdraw?.mapping?.keys?.first(),
                    value = WithdrawValue(
                        who = withdraw?.mapping?.get("who") as? ByteArray,
                        amount = withdraw?.mapping?.get("amount") as? BigInteger,
                    )
                )
            )
        }
        EventTypes.Msa.name -> {
            val msaCreated = (event.value as? DictEnum.Entry<*>)?.value as? Struct.Instance
            //val msaCreated = structInstance?.mapping?.get("MsaCreated") as? Struct.Instance
            EventType.MsaEvent(
                name = event.name,
                value = MsaCreated(
                    name = msaCreated?.mapping?.keys?.first(),
                    value = MsaValue(
                        msa_id = msaCreated?.mapping?.get("msa_id") as? BigInteger,
                        key = msaCreated?.mapping?.get("key") as? ByteArray,
                    )
                )
            )
        }
        EventTypes.TransactionPayment.name -> {
            val transactionFee = (event.value as? DictEnum.Entry<*>)?.value as? Struct.Instance
            //val transactionFee = structInstance?.mapping?.get("TransactionFeePaid") as? Struct.Instance
            EventType.TransactionPayment(
                name = event.name,
                value = TransactionFee(
                    name = transactionFee?.mapping?.keys?.first(),
                    value = TransactionFeeValue(
                        who = transactionFee?.mapping?.get("who") as? ByteArray,
                        actual_fee = transactionFee?.mapping?.get("actual_fee") as? BigInteger,
                        tip = transactionFee?.mapping?.get("tip") as? BigInteger,
                    )
                )
            )
        }
        else -> EventType.Incompaitable
    }
}

fun List<EventType>.checkIfExtrinsicFailed(): DisptachError? {
    val systems = map { it as? EventType.System }.filterNotNull()
    val error = systems.findLast { it.value.error != null }
    return error?.value?.error
}

