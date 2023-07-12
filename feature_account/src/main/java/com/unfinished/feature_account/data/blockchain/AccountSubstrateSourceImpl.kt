package com.unfinished.feature_account.data.blockchain

import com.unfinished.feature_account.data.blockchain.AccountSubstrateSource
import com.unfinished.data.network.rpc.SocketSingleRequestExecutor
import jp.co.soramitsu.fearless_utils.wsrpc.mappers.nonNull
import jp.co.soramitsu.fearless_utils.wsrpc.mappers.pojo
import jp.co.soramitsu.fearless_utils.wsrpc.request.base.RpcRequest
import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.system.NodeNetworkTypeRequest

class AccountSubstrateSourceImpl(
    private val socketRequestExecutor: SocketSingleRequestExecutor
) : AccountSubstrateSource {

    override suspend fun getNodeNetworkType(nodeHost: String): String {
        val request = NodeNetworkTypeRequest()

        return socketRequestExecutor.executeRequest(request, nodeHost, pojo<String>().nonNull())
    }
}
