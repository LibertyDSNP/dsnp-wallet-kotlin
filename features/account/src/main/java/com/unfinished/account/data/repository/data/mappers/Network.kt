package com.unfinished.account.data.repository.data.mappers

import com.unfinished.data.model.Network
import com.unfinished.data.model.Node
import com.unfinished.data.multiNetwork.chain.model.ChainId

fun stubNetwork(chainId: ChainId): Network {
    val networkType = Node.NetworkType.findByGenesis(chainId) ?: Node.NetworkType.POLKADOT

    return Network(networkType)
}
