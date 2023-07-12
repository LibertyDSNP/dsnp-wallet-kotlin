package com.unfinished.runtime.repository

import com.unfinished.runtime.network.runtime.binding.ParaId
import com.unfinished.runtime.network.runtime.binding.bindNumber
import com.unfinished.runtime.util.parachainInfoOrNull
import com.unfinished.runtime.multiNetwork.chain.model.ChainId
import com.unfinished.runtime.storage.source.StorageDataSource
import jp.co.soramitsu.fearless_utils.runtime.metadata.storage
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

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
