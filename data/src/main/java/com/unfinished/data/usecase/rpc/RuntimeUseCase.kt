package com.unfinished.data.usecase.rpc

import com.unfinished.data.multiNetwork.ChainRegistry
import com.unfinished.data.multiNetwork.chain.model.Chain
import com.unfinished.data.multiNetwork.getRuntime
import com.unfinished.data.multiNetwork.getSocket
import com.unfinished.data.multiNetwork.rpc.RpcCalls
import com.unfinished.data.repository.event.EventsRepository
import jp.co.soramitsu.fearless_utils.wsrpc.executeAsync
import jp.co.soramitsu.fearless_utils.wsrpc.mappers.nonNull
import jp.co.soramitsu.fearless_utils.wsrpc.mappers.pojo
import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.chain.RuntimeVersion
import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.chain.RuntimeVersionRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RuntimeUseCase @Inject constructor(
    private val chainRegistry: ChainRegistry,
){
    suspend fun getRuntimeVersion(chain: Chain): RuntimeVersion {
        val request = RuntimeVersionRequest()
        return chainRegistry.getSocket(chain.id)
            .executeAsync(request, mapper = pojo<RuntimeVersion>().nonNull())
    }

    suspend fun getRuntimeMetaData(chain: Chain) = chainRegistry.getRuntime(chain.id)
}