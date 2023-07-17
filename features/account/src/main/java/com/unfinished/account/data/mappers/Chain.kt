package com.unfinished.account.data.mappers

import com.unfinished.account.presentation.model.chain.ChainUi
import com.unfinished.runtime.multiNetwork.chain.model.Chain

fun mapChainToUi(chain: Chain): ChainUi = with(chain) {
    ChainUi(
        id = id,
        name = name,
        icon = icon,
    )
}
