package com.unfinished.data.multiNetwork.rpc.bulkRetriever

import com.unfinished.data.multiNetwork.runtime.binding.BlockHash
import jp.co.soramitsu.fearless_utils.wsrpc.SocketService

suspend fun BulkRetriever.retrieveAllValues(socketService: SocketService, keyPrefix: String, at: BlockHash? = null): Map<String, String?> {
    val allKeys = retrieveAllKeys(socketService, keyPrefix, at)

    return queryKeys(socketService, allKeys, at)
}
