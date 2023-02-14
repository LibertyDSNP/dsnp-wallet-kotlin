package com.unfinished.feature_account.data.mappers

import com.unfinished.feature_account.presentation.model.chain.ChainUi
import io.novafoundation.nova.runtime.multiNetwork.chain.model.Chain

fun mapChainToUi(chain: Chain): ChainUi = with(chain) {
    ChainUi(
        id = id,
        name = name,
        icon = icon,
    )
}