package com.unfinished.account.data.repository.data.mappers

import com.unfinished.account.presentation.model.chain.ChainUi
import com.unfinished.data.multiNetwork.chain.model.Chain


fun mapChainToUi(chain: Chain): ChainUi = with(chain) {
    ChainUi(
        id = id,
        name = name,
        icon = icon,
    )
}
