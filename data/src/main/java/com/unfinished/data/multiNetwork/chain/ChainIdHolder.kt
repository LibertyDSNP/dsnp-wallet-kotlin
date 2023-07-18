package com.unfinished.data.multiNetwork.chain

interface ChainIdHolder {
    suspend fun chainId(): String
}
