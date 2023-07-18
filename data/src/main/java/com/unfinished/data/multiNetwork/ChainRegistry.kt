package com.unfinished.data.multiNetwork

import com.google.gson.Gson
import com.unfinished.data.db.dao.ChainDao
import com.unfinished.data.util.diffed
import com.unfinished.data.util.inBackground
import com.unfinished.data.util.mapList
import com.unfinished.data.util.removeHexPrefix
import com.unfinished.data.multiNetwork.chain.ChainSyncService
import com.unfinished.data.multiNetwork.chain.mapChainLocalToChain
import com.unfinished.data.multiNetwork.chain.model.Chain
import com.unfinished.data.multiNetwork.chain.model.FullChainAssetId
import com.unfinished.data.multiNetwork.connection.ChainConnection
import com.unfinished.data.multiNetwork.connection.ConnectionPool
import com.unfinished.data.multiNetwork.runtime.RuntimeProvider
import com.unfinished.data.multiNetwork.runtime.RuntimeProviderPool
import com.unfinished.data.multiNetwork.runtime.RuntimeSubscriptionPool
import com.unfinished.data.multiNetwork.runtime.RuntimeSyncService
import com.unfinished.data.multiNetwork.runtime.types.BaseTypeSynchronizer
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

suspend fun _root_ide_package_.com.unfinished.data.multiNetwork.ChainRegistry.getChainOrNull(chainId: String): Chain? {
    return chainsById.first()[chainId.removeHexPrefix()]
}

suspend fun _root_ide_package_.com.unfinished.data.multiNetwork.ChainRegistry.chainWithAssetOrNull(chainId: String, assetId: Int): _root_ide_package_.com.unfinished.data.multiNetwork.ChainWithAsset? {
    val chain = getChainOrNull(chainId) ?: return null
    val chainAsset = chain.assetsById[assetId] ?: return null

    return _root_ide_package_.com.unfinished.data.multiNetwork.ChainWithAsset(chain, chainAsset)
}

suspend fun _root_ide_package_.com.unfinished.data.multiNetwork.ChainRegistry.chainWithAsset(chainId: String, assetId: Int): _root_ide_package_.com.unfinished.data.multiNetwork.ChainWithAsset {
    val chain = chainsById.first().getValue(chainId)

    return _root_ide_package_.com.unfinished.data.multiNetwork.ChainWithAsset(
        chain,
        chain.assetsById.getValue(assetId)
    )
}

suspend fun _root_ide_package_.com.unfinished.data.multiNetwork.ChainRegistry.asset(chainId: String, assetId: Int): Chain.Asset {
    val chain = chainsById.first().getValue(chainId)

    return chain.assetsById.getValue(assetId)
}
suspend fun _root_ide_package_.com.unfinished.data.multiNetwork.ChainRegistry.asset(fullChainAssetId: FullChainAssetId): Chain.Asset {
    return asset(fullChainAssetId.chainId, fullChainAssetId.assetId)
}

suspend inline fun _root_ide_package_.com.unfinished.data.multiNetwork.ChainRegistry.findChain(predicate: (Chain) -> Boolean): Chain? = currentChains.first().firstOrNull(predicate)
suspend inline fun _root_ide_package_.com.unfinished.data.multiNetwork.ChainRegistry.findChains(predicate: (Chain) -> Boolean): List<Chain> = currentChains.first().filter(predicate)

suspend fun _root_ide_package_.com.unfinished.data.multiNetwork.ChainRegistry.getRuntime(chainId: String) = getRuntimeProvider(chainId).get()

fun _root_ide_package_.com.unfinished.data.multiNetwork.ChainRegistry.getSocket(chainId: String) = getConnection(chainId).socketService

fun _root_ide_package_.com.unfinished.data.multiNetwork.ChainRegistry.getService(chainId: String) =
    _root_ide_package_.com.unfinished.data.multiNetwork.ChainService(
        runtimeProvider = getRuntimeProvider(chainId),
        connection = getConnection(chainId)
    )
