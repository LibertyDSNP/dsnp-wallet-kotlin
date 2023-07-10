package com.unfinished.runtime.multiNetwork.runtime.repository

import com.unfinished.runtime.multiNetwork.chain.model.ChainId

class RuntimeVersion(
    val chainId: ChainId,
    val specVersion: Int
)
