package com.unfinished.data.repository.chain

import com.unfinished.data.multiNetwork.runtime.binding.BlockNumber
import com.unfinished.data.multiNetwork.runtime.binding.bindBlockNumber
import com.unfinished.data.util.ext.babe
import com.unfinished.data.util.ext.babeOrNull
import com.unfinished.data.util.ext.isParachain
import com.unfinished.data.util.ext.numberConstant
import com.unfinished.data.util.ext.optionalNumberConstant
import com.unfinished.data.util.ext.system
import com.unfinished.data.util.ext.timestampOrNull
import com.unfinished.data.multiNetwork.ChainRegistry
import com.unfinished.data.multiNetwork.chain.model.ChainId
import com.unfinished.data.multiNetwork.getRuntime
import com.unfinished.data.multiNetwork.storage.SampledBlockTimeStorage
import com.unfinished.data.multiNetwork.storage.updaters.SampledBlockTime
import com.unfinished.data.multiNetwork.storage.source.StorageDataSource
import com.unfinished.data.multiNetwork.storage.source.observeNonNull
import com.unfinished.data.multiNetwork.storage.source.queryNonNull
import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import jp.co.soramitsu.fearless_utils.runtime.metadata.storage
import jp.co.soramitsu.fearless_utils.runtime.metadata.storageKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.math.BigInteger

private val FALLBACK_BLOCK_TIME_MILLIS_RELAYCHAIN = (6 * 1000).toBigInteger()
private val FALLBACK_BLOCK_TIME_MILLIS_PARACHAIN = 2.toBigInteger() * FALLBACK_BLOCK_TIME_MILLIS_RELAYCHAIN

private val PERIOD_VALIDITY_THRESHOLD = 100.toBigInteger()

private val REQUIRED_SAMPLED_BLOCKS = 10.toBigInteger()

class ChainStateRepository(
    private val localStorage: StorageDataSource,
    private val sampledBlockTimeStorage: SampledBlockTimeStorage,
    private val chainRegistry: ChainRegistry
) {

    suspend fun expectedBlockTimeInMillis(chainId: ChainId): BigInteger {
        val runtime = chainRegistry.getRuntime(chainId)

        return runtime.metadata.babe().numberConstant("ExpectedBlockTime", runtime)
    }

    suspend fun predictedBlockTime(chainId: ChainId): BigInteger {
        val runtime = chainRegistry.getRuntime(chainId)

        val blockTimeFromConstants = blockTimeFromConstants(runtime)
        val sampledBlockTime = sampledBlockTimeStorage.get(chainId)

        return weightedAverageBlockTime(sampledBlockTime, blockTimeFromConstants)
    }

    suspend fun predictedBlockTimeFlow(chainId: ChainId): Flow<BigInteger> {
        val runtime = chainRegistry.getRuntime(chainId)

        val blockTimeFromConstants = blockTimeFromConstants(runtime)

        return sampledBlockTimeStorage.observe(chainId).map {
            weightedAverageBlockTime(it, blockTimeFromConstants)
        }
    }

    private fun weightedAverageBlockTime(
        sampledBlockTime: SampledBlockTime,
        blockTimeFromConstants: BigInteger
    ): BigInteger {
        val cappedSampleSize = sampledBlockTime.sampleSize.min(REQUIRED_SAMPLED_BLOCKS)
        val sampledPart = cappedSampleSize * sampledBlockTime.averageBlockTime
        val constantsPart = (REQUIRED_SAMPLED_BLOCKS - cappedSampleSize) * blockTimeFromConstants

        return (sampledPart + constantsPart) / REQUIRED_SAMPLED_BLOCKS
    }

    private fun blockTimeFromConstants(runtime: RuntimeSnapshot): BigInteger {
        return runtime.metadata.babeOrNull()?.numberConstant("ExpectedBlockTime", runtime)
            // Some chains incorrectly use these, i.e. it is set to values such as 0 or even 2
            // Use a low minimum validity threshold to check these against
            ?: runtime.metadata.timestampOrNull()?.numberConstant("MinimumPeriod", runtime)?.takeIf { it > PERIOD_VALIDITY_THRESHOLD }
            ?: fallbackBlockTime(runtime)
    }

    suspend fun blockHashCount(chainId: ChainId): BigInteger? {
        val runtime = chainRegistry.getRuntime(chainId)

        return runtime.metadata.system().optionalNumberConstant("BlockHashCount", runtime)
    }

    suspend fun currentBlock(chainId: ChainId) = localStorage.queryNonNull(
        keyBuilder = ::currentBlockStorageKey,
        binding = { scale, runtime -> bindBlockNumber(scale, runtime) },
        chainId = chainId
    )

    fun currentBlockNumberFlow(chainId: ChainId): Flow<BlockNumber> = localStorage.observeNonNull(
        keyBuilder = ::currentBlockStorageKey,
        binding = { scale, runtime -> bindBlockNumber(scale, runtime) },
        chainId = chainId
    )

    private fun currentBlockStorageKey(runtime: RuntimeSnapshot) = runtime.metadata.system().storage("Number").storageKey()

    private fun fallbackBlockTime(runtime: RuntimeSnapshot): BigInteger {
        return if (runtime.isParachain()) {
            FALLBACK_BLOCK_TIME_MILLIS_PARACHAIN
        } else {
            FALLBACK_BLOCK_TIME_MILLIS_RELAYCHAIN
        }
    }
}
