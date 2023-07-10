package com.unfinished.runtime.network.updaters

import android.util.Log
import com.unfinished.common.data.holders.ChainIdHolder
import com.unfinished.common.data.network.runtime.binding.BlockHash
import com.unfinished.common.data.network.runtime.binding.BlockNumber
import com.unfinished.common.data.network.runtime.binding.bindNumber
import com.unfinished.common.utils.LOG_TAG
import com.unfinished.common.utils.decodeValue
import com.unfinished.common.utils.system
import com.unfinished.common.utils.timestamp
import com.unfinished.common.utils.zipWithPrevious
import com.unfinished.common.core.api.updater.GlobalScopeUpdater
import com.unfinished.common.core.api.updater.SubscriptionBuilder
import com.unfinished.common.core.api.updater.Updater
import com.unfinished.runtime.multiNetwork.ChainRegistry
import com.unfinished.runtime.multiNetwork.chain.model.ChainId
import com.unfinished.runtime.multiNetwork.getRuntime
import com.unfinished.runtime.storage.SampledBlockTimeStorage
import com.unfinished.runtime.storage.source.StorageDataSource
import jp.co.soramitsu.fearless_utils.runtime.metadata.storage
import jp.co.soramitsu.fearless_utils.runtime.metadata.storageKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.math.BigInteger

data class SampledBlockTime(
    val sampleSize: BigInteger,
    val averageBlockTime: BigInteger,
)

private data class BlockTimeUpdate(
    val at: BlockHash,
    val blockNumber: BlockNumber,
    val timestamp: BigInteger,
)

class BlockTimeUpdater(
    private val chainIdHolder: ChainIdHolder,
    private val chainRegistry: ChainRegistry,
    private val sampledBlockTimeStorage: SampledBlockTimeStorage,
    private val remoteStorageSource: StorageDataSource,
) : GlobalScopeUpdater {

    override val requiredModules: List<String> = emptyList()

    override suspend fun listenForUpdates(storageSubscriptionBuilder: SubscriptionBuilder): Flow<Updater.SideEffect> {
        val chainId = chainIdHolder.chainId()
        val runtime = chainRegistry.getRuntime(chainId)
        val storage = runtime.metadata.system().storage("Number")

        val blockNumberKey = storage.storageKey()

        return storageSubscriptionBuilder.subscribe(blockNumberKey)
            .drop(1) // ignore fist subscription value since it comes immediately
            .map {
                val timestamp = remoteStorageSource.query(chainId, at = it.block) {
                    runtime.metadata.timestamp().storage("Now").query(binding = ::bindNumber)
                }

                val blockNumber = bindNumber(storage.decodeValue(it.value, runtime))

                BlockTimeUpdate(at = it.block, blockNumber = blockNumber, timestamp = timestamp)
            }
            .zipWithPrevious()
            .filter { (previous, current) ->
                previous != null && current.blockNumber - previous.blockNumber == BigInteger.ONE
            }
            .onEach { (previousUpdate, currentUpdate) ->
                val blockTime = currentUpdate.timestamp - previousUpdate!!.timestamp

                updateSampledBlockTime(chainId, blockTime)
            }.noSideAffects()
    }

    private suspend fun updateSampledBlockTime(chainId: ChainId, newSampledTime: BigInteger) {
        val current = sampledBlockTimeStorage.get(chainId)

        val adjustedSampleSize = current.sampleSize + BigInteger.ONE
        val adjustedAverage = (current.averageBlockTime * current.sampleSize + newSampledTime) / adjustedSampleSize
        val adjustedSampledBlockTime = SampledBlockTime(
            sampleSize = adjustedSampleSize,
            averageBlockTime = adjustedAverage
        )

        Log.d(LOG_TAG, "New block time update: $newSampledTime, adjustedAverage: $adjustedSampledBlockTime")

        sampledBlockTimeStorage.put(chainId, adjustedSampledBlockTime)
    }
}
