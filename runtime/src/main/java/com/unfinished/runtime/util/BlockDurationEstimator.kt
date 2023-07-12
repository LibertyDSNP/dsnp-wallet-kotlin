package com.unfinished.runtime.util

import com.unfinished.data.network.runtime.binding.BlockNumber
import com.unfinished.data.util.formating.TimerValue
import com.unfinished.data.util.formating.toTimerValue
import java.math.BigInteger
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

interface BlockDurationEstimator {

    val currentBlock: BlockNumber

    fun durationUntil(block: BlockNumber): Duration

    fun durationOf(blocks: BlockNumber): Duration

    fun timestampOf(block: BlockNumber): Long
}

fun BlockDurationEstimator.timerUntil(block: BlockNumber): TimerValue {
    return durationUntil(block).toTimerValue()
}

fun BlockDurationEstimator(currentBlock: BlockNumber, blockTimeMillis: BigInteger): BlockDurationEstimator {
    return RealBlockDurationEstimator(currentBlock, blockTimeMillis)
}

internal class RealBlockDurationEstimator(
    override val currentBlock: BlockNumber,
    private val blockTimeMillis: BigInteger
) : BlockDurationEstimator {

    override fun durationUntil(block: BlockNumber): Duration {
        val blocksInFuture = block - currentBlock

        return durationOf(blocksInFuture)
    }

    override fun durationOf(blocks: BlockNumber): Duration {
        if (blocks < BigInteger.ZERO) return Duration.ZERO

        val millisInFuture = blocks * blockTimeMillis

        return millisInFuture.toLong().milliseconds
    }

    override fun timestampOf(block: BlockNumber): Long {
        val offsetInBlocks = block - currentBlock
        val offsetInMillis = offsetInBlocks * blockTimeMillis

        val currentTime = System.currentTimeMillis()

        return currentTime + offsetInMillis.toLong()
    }
}
