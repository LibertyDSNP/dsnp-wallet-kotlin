package com.unfinished.runtime.multiNetwork.chain.remote

import com.unfinished.runtime.multiNetwork.chain.remote.model.ChainRemote
import retrofit2.http.GET

interface ChainFetcher {

    @GET(com.unfinished.runtime.BuildConfig.CHAINS_URL)
    suspend fun getChains(): List<ChainRemote>
}
