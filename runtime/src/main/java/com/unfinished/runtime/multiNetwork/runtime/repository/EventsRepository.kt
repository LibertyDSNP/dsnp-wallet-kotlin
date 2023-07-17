package com.unfinished.runtime.multiNetwork.runtime.repository

import com.unfinished.runtime.network.runtime.binding.*
import com.unfinished.runtime.network.runtime.model.event.EventType
import com.unfinished.runtime.util.extrinsicHash
import com.unfinished.runtime.util.msa
import com.unfinished.runtime.util.system
import com.unfinished.runtime.extrinsic.create
import com.unfinished.runtime.multiNetwork.ChainRegistry
import com.unfinished.runtime.multiNetwork.chain.model.Chain
import com.unfinished.runtime.multiNetwork.chain.model.ChainId
import com.unfinished.runtime.multiNetwork.getRuntime
import com.unfinished.runtime.network.rpc.RpcCalls
import com.unfinished.runtime.storage.source.StorageDataSource
import com.unfinished.runtime.storage.source.queryNonNull
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
    private val chainRegistry: ChainRegistry,
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
