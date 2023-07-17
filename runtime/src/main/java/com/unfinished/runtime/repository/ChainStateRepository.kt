package com.unfinished.runtime.repository

import com.unfinished.runtime.network.runtime.binding.BlockNumber
import com.unfinished.runtime.network.runtime.binding.bindBlockNumber
import com.unfinished.runtime.util.babe
import com.unfinished.runtime.util.babeOrNull
import com.unfinished.runtime.util.isParachain
import com.unfinished.runtime.util.numberConstant
import com.unfinished.runtime.util.optionalNumberConstant
import com.unfinished.runtime.util.system
import com.unfinished.runtime.util.timestampOrNull
import com.unfinished.runtime.multiNetwork.ChainRegistry
import com.unfinished.runtime.multiNetwork.chain.model.ChainId
import com.unfinished.runtime.multiNetwork.getRuntime
import com.unfinished.runtime.network.updaters.SampledBlockTime
import com.unfinished.runtime.storage.SampledBlockTimeStorage
import com.unfinished.runtime.storage.source.StorageDataSource
import com.unfinished.runtime.storage.source.observeNonNull
import com.unfinished.runtime.storage.source.queryNonNull
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
