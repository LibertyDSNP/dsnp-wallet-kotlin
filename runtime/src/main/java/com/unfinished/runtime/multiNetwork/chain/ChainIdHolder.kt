package com.unfinished.runtime.multiNetwork.chain

interface ChainIdHolder {
    suspend fun chainId(): String
}
