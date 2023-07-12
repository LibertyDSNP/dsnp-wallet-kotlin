package com.unfinished.data.network

import com.unfinished.data.model.StorageChange
import com.unfinished.data.updater.SubscriptionBuilder
import jp.co.soramitsu.fearless_utils.wsrpc.SocketService
import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.storage.StorageSubscriptionMultiplexer
import jp.co.soramitsu.fearless_utils.wsrpc.subscribe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StorageSubscriptionBuilder(
    override val socketService: SocketService,
    private val proxy: StorageSubscriptionMultiplexer.Builder
) : SubscriptionBuilder {

    companion object {

        fun create(socketService: SocketService): StorageSubscriptionBuilder {
            val proxy = StorageSubscriptionMultiplexer.Builder()

            return StorageSubscriptionBuilder(socketService, proxy)
        }
    }

    override fun subscribe(key: String): Flow<StorageChange> {
        return proxy.subscribe(key)
            .map { StorageChange(it.block, it.key, it.value) }
    }

    fun build() = proxy.build()
}
