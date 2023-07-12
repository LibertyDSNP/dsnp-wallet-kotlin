package com.unfinished.runtime.network.updaters

import com.unfinished.data.storage.cache.StorageCache
import com.unfinished.runtime.multiNetwork.chain.ChainIdHolder
import com.unfinished.runtime.util.Modules
import com.unfinished.runtime.util.balances
import com.unfinished.runtime.multiNetwork.ChainRegistry
import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import jp.co.soramitsu.fearless_utils.runtime.metadata.storage
import jp.co.soramitsu.fearless_utils.runtime.metadata.storageKey

class TotalIssuanceUpdater(
    chainIdHolder: ChainIdHolder,
    storageCache: StorageCache,
    chainRegistry: ChainRegistry
) : SingleStorageKeyUpdater<GlobalScope>(GlobalScope, chainIdHolder, chainRegistry, storageCache) {

    override val requiredModules: List<String> = listOf(Modules.BALANCES)

    override suspend fun storageKey(runtime: RuntimeSnapshot): String {
        return runtime.metadata.balances().storage("TotalIssuance").storageKey()
    }
}
