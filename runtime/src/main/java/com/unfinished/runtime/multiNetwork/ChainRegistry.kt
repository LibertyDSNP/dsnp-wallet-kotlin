package com.unfinished.runtime.multiNetwork

import com.google.gson.Gson
import com.unfinished.data.db.dao.ChainDao
import com.unfinished.data.util.diffed
import com.unfinished.data.util.inBackground
import com.unfinished.data.util.mapList
import com.unfinished.runtime.util.removeHexPrefix
import com.unfinished.runtime.multiNetwork.chain.ChainSyncService
import com.unfinished.runtime.multiNetwork.chain.mapChainLocalToChain
import com.unfinished.runtime.multiNetwork.chain.model.Chain
import com.unfinished.runtime.multiNetwork.chain.model.FullChainAssetId
import com.unfinished.runtime.multiNetwork.connection.ChainConnection
import com.unfinished.runtime.multiNetwork.connection.ConnectionPool
import com.unfinished.runtime.multiNetwork.runtime.RuntimeProvider
import com.unfinished.runtime.multiNetwork.runtime.RuntimeProviderPool
import com.unfinished.runtime.multiNetwork.runtime.RuntimeSubscriptionPool
import com.unfinished.runtime.multiNetwork.runtime.RuntimeSyncService
import com.unfinished.runtime.multiNetwork.runtime.types.BaseTypeSynchronizer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

data class ChainService(
    val runtimeProvider: RuntimeProvider,
    val connection: ChainConnection
)

data class ChainWithAsset(
    val chain: Chain,
    val asset: Chain.Asset
)

class ChainRegistry(
    private val runtimeProviderPool: RuntimeProviderPool,
    private val connectionPool: ConnectionPool,
    private val runtimeSubscriptionPool: RuntimeSubscriptionPool,
    private val chainDao: ChainDao,
    private val chainSyncService: ChainSyncService,
    private val baseTypeSynchronizer: BaseTypeSynchronizer,
    private val runtimeSyncService: RuntimeSyncService,
    private val gson: Gson
) : CoroutineScope by CoroutineScope(Dispatchers.Default) {

    val currentChains = chainDao.joinChainInfoFlow()
        .mapList { mapChainLocalToChain(it, gson) }
        .diffed()
        .map { (removed, addedOrModified, all) ->
            removed.forEach {
                val chainId = it.id

                runtimeProviderPool.removeRuntimeProvider(chainId)
                runtimeSubscriptionPool.removeSubscription(chainId)
                runtimeSyncService.unregisterChain(chainId)
                connectionPool.removeConnection(chainId)
            }

            addedOrModified.forEach { chain ->
                val connection = connectionPool.setupConnection(chain)

                runtimeProviderPool.setupRuntimeProvider(chain)
                runtimeSyncService.registerChain(chain, connection)
                runtimeSubscriptionPool.setupRuntimeSubscription(chain, connection)
                runtimeProviderPool.setupRuntimeProvider(chain)
            }

            all
        }
        .filter { it.isNotEmpty() }
        .distinctUntilChanged()
        .inBackground()
        .shareIn(this, SharingStarted.Eagerly, replay = 1)

    val chainsById = currentChains.map { chains -> chains.associateBy { it.id } }
        .inBackground()
        .shareIn(this, SharingStarted.Eagerly, replay = 1)

    init {
        launch { chainSyncService.syncUp() }

        baseTypeSynchronizer.sync()
    }

    fun getConnection(chainId: String) = connectionPool.getConnection(chainId.removeHexPrefix())

    fun getRuntimeProvider(chainId: String) = runtimeProviderPool.getRuntimeProvider(chainId.removeHexPrefix())

    suspend fun getChain(chainId: String): Chain = chainsById.first().getValue(chainId.removeHexPrefix())
}

suspend fun ChainRegistry.getChainOrNull(chainId: String): Chain? {
    return chainsById.first()[chainId.removeHexPrefix()]
}

suspend fun ChainRegistry.chainWithAssetOrNull(chainId: String, assetId: Int): ChainWithAsset? {
    val chain = getChainOrNull(chainId) ?: return null
    val chainAsset = chain.assetsById[assetId] ?: return null

    return ChainWithAsset(chain, chainAsset)
}

suspend fun ChainRegistry.chainWithAsset(chainId: String, assetId: Int): ChainWithAsset {
    val chain = chainsById.first().getValue(chainId)

    return ChainWithAsset(chain, chain.assetsById.getValue(assetId))
}

suspend fun ChainRegistry.asset(chainId: String, assetId: Int): Chain.Asset {
    val chain = chainsById.first().getValue(chainId)

    return chain.assetsById.getValue(assetId)
}
suspend fun ChainRegistry.asset(fullChainAssetId: FullChainAssetId): Chain.Asset {
    return asset(fullChainAssetId.chainId, fullChainAssetId.assetId)
}

suspend inline fun ChainRegistry.findChain(predicate: (Chain) -> Boolean): Chain? = currentChains.first().firstOrNull(predicate)
suspend inline fun ChainRegistry.findChains(predicate: (Chain) -> Boolean): List<Chain> = currentChains.first().filter(predicate)

suspend fun ChainRegistry.getRuntime(chainId: String) = getRuntimeProvider(chainId).get()

fun ChainRegistry.getSocket(chainId: String) = getConnection(chainId).socketService

fun ChainRegistry.getService(chainId: String) = ChainService(
    runtimeProvider = getRuntimeProvider(chainId),
    connection = getConnection(chainId)
)
