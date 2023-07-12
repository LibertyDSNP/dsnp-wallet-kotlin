package com.unfinished.runtime.network.rpc.bulkRetriever

import com.unfinished.runtime.network.runtime.binding.BlockHash
import jp.co.soramitsu.fearless_utils.wsrpc.SocketService

suspend fun BulkRetriever.retrieveAllValues(socketService: SocketService, keyPrefix: String, at: BlockHash? = null): Map<String, String?> {
    val allKeys = retrieveAllKeys(socketService, keyPrefix, at)

    return queryKeys(socketService, allKeys, at)
}
