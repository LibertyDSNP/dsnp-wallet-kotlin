package com.unfinished.runtime.util

import android.os.Build
import com.unfinished.runtime.multiNetwork.chain.model.Chain
import java8.util.Comparators

val Chain.relaychainsFirstAscendingOrder
    get() = when (genesisHash) {
        ChainGeneses.POLKADOT -> 0
        ChainGeneses.KUSAMA -> 1
        else -> 2
    }

val Chain.testnetsLastAscendingOrder
    get() = if (isTestNet) {
        1
    } else {
        0
    }

val Chain.alphabeticalOrder
    get() = name

fun <K> Chain.Companion.defaultComparatorFrom(extractor: (K) -> Chain): Comparator<K> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    Comparator.comparing(extractor, defaultComparator())
} else {
    Comparators.comparing(extractor, defaultComparator())
}

fun Chain.Companion.defaultComparator(): Comparator<Chain> = compareBy<Chain> { it.relaychainsFirstAscendingOrder }
    .thenBy { it.testnetsLastAscendingOrder }
    .thenBy { it.alphabeticalOrder }
