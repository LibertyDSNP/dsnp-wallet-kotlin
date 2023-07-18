package com.unfinished.data.repository.event

import com.unfinished.data.multiNetwork.runtime.binding.*
import com.unfinished.data.util.extrinsicHash
import com.unfinished.data.util.msa
import com.unfinished.data.util.system
import com.unfinished.data.multiNetwork.chain.model.Chain
import com.unfinished.data.multiNetwork.chain.model.ChainId
import com.unfinished.data.multiNetwork.extrinsic.create
import com.unfinished.data.multiNetwork.getRuntime
import com.unfinished.data.multiNetwork.rpc.RpcCalls
import com.unfinished.data.model.EventType
import com.unfinished.data.multiNetwork.storage.source.StorageDataSource
import com.unfinished.data.multiNetwork.storage.source.queryNonNull
import jp.co.soramitsu.fearless_utils.runtime.AccountId
import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.fromHexOrNull
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.generics.Extrinsic
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.generics.GenericEvent
import jp.co.soramitsu.fearless_utils.runtime.metadata.storage
import jp.co.soramitsu.fearless_utils.runtime.metadata.storageKey
import java.math.BigInteger

interface EventsRepository {

    /**
     * @return events in block corresponding to [blockHash] or in current block, if [blockHash] is null
     * Unparsed events are not included
     */
    suspend fun getEventsInBlockForFrequency(chainId: ChainId, blockHash: BlockHash? = null):  Result<List<EventType>>
    suspend fun getMsaIdFromFrequencyChain(chain: Chain, runtimeSnapshot: RuntimeSnapshot, accountId: AccountId):  Result<BigInteger?>
    suspend fun getEventsInBlock(chainId: ChainId, blockHash: BlockHash? = null): List<EventRecord>

    /**
     * @return extrinsics with their event in block corresponding to [blockHash] or in current block, if [blockHash] is null
     * Unparsed events & extrinsics are not included
     */
    suspend fun getExtrinsicsWithEvents(chainId: ChainId, blockHash: BlockHash? = null): List<ExtrinsicWithEvents>
}

class RemoteEventsRepository(
    private val rpcCalls: RpcCalls,
    private val chainRegistry: _root_ide_package_.com.unfinished.data.multiNetwork.ChainRegistry,
    private val remoteStorageSource: StorageDataSource
) : EventsRepository {

    override suspend fun getEventsInBlockForFrequency(chainId: ChainId, blockHash: BlockHash?): Result<List<EventType>> {
        return remoteStorageSource.queryNonNull(
            chainId = chainId,
            keyBuilder = { it.metadata.system().storage("Events").storageKey() },
            binding = { scale, runtime ->
                bindEventRecordsForFrequency(scale, runtime)
            },
            at = blockHash
        )
    }
//
    override suspend fun getMsaIdFromFrequencyChain(chain: Chain, runtimeSnapshot: RuntimeSnapshot, accountId: AccountId): Result<BigInteger?> {
        return remoteStorageSource.query(
            chainId = chain.id,
            keyBuilder = { it.metadata.msa().storage("PublicKeyToMsaId").storageKey(runtimeSnapshot,accountId) },
            binding = { scale, runtime ->
                bindMsaRecordsForFrequency(scale, runtime)
            }
        )
    }

    override suspend fun getEventsInBlock(chainId: ChainId, blockHash: BlockHash?): List<EventRecord> {
        return remoteStorageSource.queryNonNull(
            chainId = chainId,
            keyBuilder = { it.metadata.system().storage("Events").storageKey() },
            binding = { scale, runtime ->
                bindEventRecords(scale, runtime)
            },
            at = blockHash
        )
    }

    override suspend fun getExtrinsicsWithEvents(
        chainId: ChainId,
        blockHash: BlockHash?
    ): List<ExtrinsicWithEvents> {
        val runtime = chainRegistry.getRuntime(chainId)
        val extrinsicType = Extrinsic.create(runtime)

        val block = rpcCalls.getBlock(chainId, blockHash)
        val events = getEventsInBlock(chainId, blockHash)

        val eventsByExtrinsicIndex: Map<Int, List<GenericEvent.Instance>> = events.mapNotNull { eventRecord ->
            (eventRecord.phase as? Phase.ApplyExtrinsic)?.let {
                it.extrinsicId.toInt() to eventRecord.event
            }
        }.groupBy(
            keySelector = { it.first },
            valueTransform = { it.second }
        )

        return block.block.extrinsics.mapIndexed { index, extrinsicScale ->
            val decodedExtrinsic = extrinsicType.fromHexOrNull(runtime, extrinsicScale)

            decodedExtrinsic?.let {
                val extrinsicEvents = eventsByExtrinsicIndex[index] ?: emptyList()

                ExtrinsicWithEvents(
                    extrinsic = decodedExtrinsic,
                    extrinsicHash = extrinsicScale.extrinsicHash(),
                    events = extrinsicEvents
                )
            }
        }.filterNotNull()
    }
}
