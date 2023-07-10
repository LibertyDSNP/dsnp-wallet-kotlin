package com.unfinished.runtime.multiNetwork.chain

import com.google.gson.Gson
import com.unfinished.common.utils.CollectionDiffer
import com.unfinished.common.utils.Identifiable
import com.unfinished.common.utils.map
import com.unfinished.common.utils.retryUntilDone
import com.unfinished.core.db.dao.ChainDao
import com.unfinished.runtime.multiNetwork.chain.model.Chain
import com.unfinished.runtime.multiNetwork.chain.remote.ChainFetcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChainSyncService(
    private val dao: ChainDao,
    private val chainFetcher: ChainFetcher,
    private val gson: Gson
) {

    suspend fun syncUp() = withContext(Dispatchers.Default) {
        val localChainsJoinedInfo = dao.getJoinChainInfo()

        val remoteChains = retryUntilDone { chainFetcher.getChains() }.map(::mapChainRemoteToChain)
        val localChains = localChainsJoinedInfo.map { mapChainLocalToChain(it, gson) }

        val chainsDiff = CollectionDiffer.findDiff(newItems = remoteChains, oldItems = localChains, forceUseNewItems = false)
            .map { mapChainToChainLocal(it, gson) }

        dao.applyDiff(
            chainDiff = chainsDiff,
            assetsDiff = nestedCollectionDiff(
                newChains = remoteChains,
                oldChains = localChains,
                collection = Chain::assets,
                domainToLocalMapper = { mapChainAssetToLocal(it, gson) }
            ),
            nodesDiff = nestedCollectionDiff(
                newChains = remoteChains,
                oldChains = localChains,
                collection = Chain::nodes,
                domainToLocalMapper = ::mapChainNodeToLocal
            ),
            explorersDiff = nestedCollectionDiff(
                newChains = remoteChains,
                oldChains = localChains,
                collection = Chain::explorers,
                domainToLocalMapper = ::mapChainExplorersToLocal
            ),
        )
    }

    private fun <T : Identifiable, R> nestedCollectionDiff(
        newChains: List<Chain>,
        oldChains: List<Chain>,
        collection: (Chain) -> List<T>,
        domainToLocalMapper: (T) -> R
    ): CollectionDiffer.Diff<R> {
        val old = oldChains.flatMap(collection)
        val new = newChains.flatMap(collection)

        val diffed = CollectionDiffer.findDiff(newItems = new, oldItems = old, forceUseNewItems = false)

        return diffed.map(domainToLocalMapper)
    }
}
