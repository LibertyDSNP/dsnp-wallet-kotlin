package com.unfinished.data.usecase.rpc

import com.unfinished.data.multiNetwork.chain.model.Chain
import com.unfinished.data.multiNetwork.rpc.RpcCalls
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeeUseCase @Inject constructor(
    private val rpcCalls: RpcCalls,
){
    suspend fun getExtrinsicFee (chain: Chain, extrinsic: String) = rpcCalls.getExtrinsicFee(chain.id,extrinsic = extrinsic)
}