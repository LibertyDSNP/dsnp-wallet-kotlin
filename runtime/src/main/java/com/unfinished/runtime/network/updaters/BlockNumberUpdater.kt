package com.unfinished.runtime.network.updaters

import com.unfinished.common.utils.Modules
import com.unfinished.common.utils.system
import com.unfinished.common.core.api.storage.StorageCache
import com.unfinished.common.core.api.updater.GlobalScope
import com.unfinished.runtime.multiNetwork.ChainRegistry
import com.unfinished.runtime.state.SingleAssetSharedState
import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import jp.co.soramitsu.fearless_utils.runtime.metadata.storage
import jp.co.soramitsu.fearless_utils.runtime.metadata.storageKey

class BlockNumberUpdater(
    chainRegistry: ChainRegistry,
    crowdloanSharedState: SingleAssetSharedState,
    storageCache: StorageCache
) : SingleStorageKeyUpdater<GlobalScope>(GlobalScope, crowdloanSharedState, chainRegistry, storageCache) {

    override suspend fun storageKey(runtime: RuntimeSnapshot): String {
        return runtime.metadata.system().storage("Number").storageKey()
    }

    override val requiredModules = listOf(Modules.SYSTEM)
}
