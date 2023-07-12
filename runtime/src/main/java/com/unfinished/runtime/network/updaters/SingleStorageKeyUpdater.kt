package com.unfinished.runtime.network.updaters

import com.unfinished.data.model.StorageChange
import com.unfinished.data.model.StorageEntry
import com.unfinished.data.storage.StorageCache
import com.unfinished.data.updater.SubscriptionBuilder
import com.unfinished.data.updater.UpdateScope
import com.unfinished.data.updater.Updater
import com.unfinished.data.holders.ChainIdHolder
import com.unfinished.runtime.multiNetwork.ChainRegistry
import com.unfinished.runtime.multiNetwork.getRuntime
import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

suspend fun StorageCache.insert(
    storageChange: StorageChange,
    chainId: String,
) {
    val storageEntry = StorageEntry(
        storageKey = storageChange.key,
        content = storageChange.value,
    )

    insert(storageEntry, chainId)
}

abstract class SingleStorageKeyUpdater<S : UpdateScope>(
    override val scope: S,
    private val chainIdHolder: ChainIdHolder,
    private val chainRegistry: ChainRegistry,
    private val storageCache: StorageCache
) : Updater {

    /**
     * @return a storage key to update. null in case updater does not want to update anything
     */
    abstract suspend fun storageKey(runtime: RuntimeSnapshot): String?

    protected open fun fallbackValue(runtime: RuntimeSnapshot): String? = null

    override suspend fun listenForUpdates(storageSubscriptionBuilder: SubscriptionBuilder): Flow<Updater.SideEffect> {
        val chainId = chainIdHolder.chainId()
        val runtime = chainRegistry.getRuntime(chainId)

        val storageKey = runCatching { storageKey(runtime) }.getOrNull() ?: return emptyFlow()

        return storageSubscriptionBuilder.subscribe(storageKey)
            .map {
                if (it.value == null) {
                    it.copy(value = fallbackValue(runtime))
                } else {
                    it
                }
            }
            .onEach { storageCache.insert(it, chainId) }
            .noSideAffects()
    }
}
