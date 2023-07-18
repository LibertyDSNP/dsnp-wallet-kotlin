package com.unfinished.data.multiNetwork.storage.updaters

import com.unfinished.data.model.StorageChange
import jp.co.soramitsu.fearless_utils.wsrpc.SocketService
import kotlinx.coroutines.flow.Flow

interface SubscriptionBuilder {
    fun subscribe(key: String): Flow<StorageChange>
}
