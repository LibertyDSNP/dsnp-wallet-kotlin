package com.unfinished.account.data.blockchain

interface AccountSubstrateSource {

    /**
     * @throws NovaException
     */
    suspend fun getNodeNetworkType(nodeHost: String): String
}
