package com.unfinished.runtime.storage.source

import com.unfinished.runtime.network.runtime.binding.BlockHash
import com.unfinished.runtime.network.runtime.calls.GetChildStateRequest
import com.unfinished.runtime.multiNetwork.ChainRegistry
import com.unfinished.runtime.multiNetwork.getSocket
import com.unfinished.runtime.network.rpc.bulkRetriever.BulkRetriever
import com.unfinished.runtime.network.rpc.bulkRetriever.queryKey
import com.unfinished.runtime.storage.source.query.RemoteStorageQueryContext
import com.unfinished.runtime.storage.source.query.StorageQueryContext
import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import jp.co.soramitsu.fearless_utils.wsrpc.executeAsync
import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.storage.SubscribeStorageRequest
import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.storage.storageChange
import jp.co.soramitsu.fearless_utils.wsrpc.subscriptionFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RemoteStorageSource @Inject constructor(
    chainRegistry: ChainRegistry,
    private val bulkRetriever: BulkRetriever,
) : BaseStorageSource(chainRegistry) {

    override suspend fun query(key: String, chainId: String, at: BlockHash?): String? {
        return bulkRetriever.queryKey(getSocketService(chainId), key, at)
    }

    override suspend fun observe(key: String, chainId: String): Flow<String?> {
        return getSocketService(chainId).subscriptionFlow(SubscribeStorageRequest(key))
            .map { it.storageChange().getSingleChange() }
    }

    override suspend fun queryChildState(storageKey: String, childKey: String, chainId: String): String? {
        val response = getSocketService(chainId).executeAsync(GetChildStateRequest(storageKey, childKey))

        return response.result as? String?
    }

    override suspend fun createQueryContext(chainId: String, at: BlockHash?, runtime: RuntimeSnapshot): StorageQueryContext {
        return RemoteStorageQueryContext(bulkRetriever, getSocketService(chainId), at, runtime)
    }

    private fun getSocketService(chainId: String) = chainRegistry.getSocket(chainId)
}
