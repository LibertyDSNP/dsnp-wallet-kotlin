package com.unfinished.data.multiNetwork.extrinsic.blockchain

interface AccountSubstrateSource {

    /**
     * @throws NovaException
     */
    suspend fun getNodeNetworkType(nodeHost: String): String
}
