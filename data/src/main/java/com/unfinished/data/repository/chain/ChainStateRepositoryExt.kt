package com.unfinished.data.repository.chain

import com.unfinished.data.multiNetwork.chain.model.ChainId
import com.unfinished.data.util.BlockDurationEstimator

suspend fun ChainStateRepository.blockDurationEstimator(chainId: ChainId): BlockDurationEstimator {
    return BlockDurationEstimator(
        currentBlock = currentBlock(chainId),
        blockTimeMillis = predictedBlockTime(chainId)
    )
}
