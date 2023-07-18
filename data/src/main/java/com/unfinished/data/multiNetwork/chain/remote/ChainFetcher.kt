package com.unfinished.data.multiNetwork.chain.remote

import com.unfinished.data.BuildConfig
import com.unfinished.data.multiNetwork.chain.remote.model.ChainRemote
import retrofit2.http.GET

interface ChainFetcher {

    @GET(BuildConfig.CHAINS_URL)
    suspend fun getChains(): List<ChainRemote>
}
