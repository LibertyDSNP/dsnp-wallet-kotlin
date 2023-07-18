package com.unfinished.data.multiNetwork.extrinsic

import com.unfinished.data.util.invoke
import com.unfinished.data.multiNetwork.chain.model.ChainId
import com.unfinished.data.multiNetwork.rpc.RpcCalls
import com.unfinished.data.repository.chain.ChainStateRepository
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.generics.Era
import kotlinx.coroutines.*
import java.lang.Integer.min

private const val FALLBACK_MAX_HASH_COUNT = 250
private const val MAX_FINALITY_LAG = 5
private const val MORTAL_PERIOD = 5 * 60 * 1000

class Mortality(val era: Era.Mortal, val blockHash: String)

class MortalityConstructor(
    private val rpcCalls: RpcCalls,
    private val chainStateRepository: ChainStateRepository,
) {

    fun mortalPeriodMillis(): Long = MORTAL_PERIOD.toLong()

    suspend fun constructMortality(chainId: ChainId): Mortality = withContext(Dispatchers.IO) {
        val finalizedHash = async { rpcCalls.getFinalizedHead(chainId) }

        val bestHeader = async { rpcCalls.getBlockHeader(chainId) }
        val finalizedHeader = async { rpcCalls.getBlockHeader(chainId, finalizedHash()) }

        //Parent hash throwing exception
        //val currentHeader = async { bestHeader().parentHash?.let { rpcCalls.getBlockHeader(chainId, it) } ?: bestHeader() }

        val currentNumber = bestHeader().number
        val finalizedNumber = finalizedHeader().number

        val startBlockNumber = if (currentNumber - finalizedNumber > MAX_FINALITY_LAG) currentNumber else finalizedNumber

        val blockHashCount = chainStateRepository.blockHashCount(chainId)?.toInt()

        val blockTime = chainStateRepository.predictedBlockTime(chainId).toInt()

        val mortalPeriod = MORTAL_PERIOD / blockTime + MAX_FINALITY_LAG

        val unmappedPeriod = min(blockHashCount ?: FALLBACK_MAX_HASH_COUNT, mortalPeriod)

        val era = Era.getEraFromBlockPeriod(startBlockNumber, unmappedPeriod)
        val eraBlockNumber = ((startBlockNumber - era.phase) / era.period) * era.period + era.phase

        val eraBlockHash = rpcCalls.getBlockHash(chainId, eraBlockNumber.toBigInteger())

        Mortality(era, eraBlockHash)
    }
}
