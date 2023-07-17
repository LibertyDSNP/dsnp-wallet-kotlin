package com.unfinished.runtime.repository

import com.unfinished.runtime.multiNetwork.chain.model.ChainId
import com.unfinished.runtime.util.BlockDurationEstimator

suspend fun ChainStateRepository.blockDurationEstimator(chainId: ChainId): BlockDurationEstimator {
    return BlockDurationEstimator(
        currentBlock = currentBlock(chainId),
        blockTimeMillis = predictedBlockTime(chainId)
    )
}
