package com.unfinished.data.repository.chain

import com.unfinished.data.multiNetwork.runtime.binding.ParaId
import com.unfinished.data.multiNetwork.runtime.binding.bindNumber
import com.unfinished.data.util.ext.parachainInfoOrNull
import com.unfinished.data.multiNetwork.chain.model.ChainId
import com.unfinished.data.multiNetwork.storage.source.StorageDataSource
import jp.co.soramitsu.fearless_utils.runtime.metadata.storage
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/*
    TODO: It's not using but leave it incase we need it in future
 */

interface ParachainInfoRepository {

    suspend fun paraId(chainId: ChainId): ParaId?
}

internal class RealParachainInfoRepository(
    private val remoteStorageSource: StorageDataSource,
) : ParachainInfoRepository {

    private val paraIdCacheMutex = Mutex()
    private val paraIdCache = mutableMapOf<ChainId, ParaId?>()

    override suspend fun paraId(chainId: ChainId): ParaId? = paraIdCacheMutex.withLock {
        if (chainId in paraIdCache) {
            paraIdCache.getValue(chainId)
        } else {
            remoteStorageSource.query(chainId) {
                runtime.metadata.parachainInfoOrNull()?.storage("ParachainId")?.query(binding = ::bindNumber)
            }
                .also { paraIdCache[chainId] = it }
        }
    }
}
