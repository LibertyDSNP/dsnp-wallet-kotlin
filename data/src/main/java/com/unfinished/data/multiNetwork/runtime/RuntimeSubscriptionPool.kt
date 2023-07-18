package com.unfinished.data.multiNetwork.runtime

import com.unfinished.data.db.dao.ChainDao
import com.unfinished.data.multiNetwork.chain.model.Chain
import com.unfinished.data.multiNetwork.connection.ChainConnection
import kotlinx.coroutines.cancel
import java.util.concurrent.ConcurrentHashMap

class RuntimeSubscriptionPool(
    private val chainDao: ChainDao,
    private val runtimeSyncService: RuntimeSyncService
) {

    private val pool = ConcurrentHashMap<String, RuntimeVersionSubscription>()

    fun getRuntimeSubscription(chainId: String) = pool.getValue(chainId)

    fun setupRuntimeSubscription(chain: Chain, connection: ChainConnection): RuntimeVersionSubscription {
        return pool.getOrPut(chain.id) {
            RuntimeVersionSubscription(chain.id, connection, chainDao, runtimeSyncService)
        }
    }

    fun removeSubscription(chainId: String) {
        pool.remove(chainId)?.apply { cancel() }
    }
}
