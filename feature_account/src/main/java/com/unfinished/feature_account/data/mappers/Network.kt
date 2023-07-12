package com.unfinished.feature_account.data.mappers

import com.unfinished.data.model.Network
import com.unfinished.data.model.Node
import com.unfinished.runtime.multiNetwork.chain.model.ChainId

fun stubNetwork(chainId: ChainId): Network {
    val networkType = Node.NetworkType.findByGenesis(chainId) ?: Node.NetworkType.POLKADOT

    return Network(networkType)
}
