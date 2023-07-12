package com.unfinished.data.updater

import com.unfinished.data.model.StorageChange
import jp.co.soramitsu.fearless_utils.wsrpc.SocketService
import kotlinx.coroutines.flow.Flow

interface SubscriptionBuilder {

    val socketService: SocketService

    fun subscribe(key: String): Flow<StorageChange>
}
