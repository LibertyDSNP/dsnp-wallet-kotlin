package com.unfinished.common.core.api.updater

import com.unfinished.common.core.api.model.StorageChange
import jp.co.soramitsu.fearless_utils.wsrpc.SocketService
import kotlinx.coroutines.flow.Flow

interface SubscriptionBuilder {

    val socketService: SocketService

    fun subscribe(key: String): Flow<StorageChange>
}
