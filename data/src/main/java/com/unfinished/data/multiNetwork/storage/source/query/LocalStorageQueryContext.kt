package com.unfinished.data.multiNetwork.storage.source.query

import com.unfinished.data.multiNetwork.runtime.binding.BlockHash
import com.unfinished.data.model.StorageEntry
import com.unfinished.data.storage.cache.StorageCache
import com.unfinished.data.multiNetwork.chain.model.ChainId
import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class LocalStorageQueryContext(
    private val storageCache: StorageCache,
    private val chainId: ChainId,
    at: BlockHash?,
    runtime: RuntimeSnapshot
) : BaseStorageQueryContext(runtime, at) {

    override suspend fun queryKeysByPrefix(prefix: String, at: BlockHash?): List<String> {
        return storageCache.getKeys(prefix, chainId)
    }

    override suspend fun queryEntriesByPrefix(prefix: String, at: BlockHash?): Map<String, String?> {
        return observeKeysByPrefix(prefix)
            .filter { it.isNotEmpty() }
            .first()
    }

    override suspend fun queryKeys(keys: List<String>, at: BlockHash?): Map<String, String?> {
        return storageCache.getEntries(keys, chainId).toMap()
    }

    override suspend fun queryKey(key: String, at: BlockHash?): String? {
        return storageCache.getEntry(key, chainId).content
    }

    override suspend fun observeKey(key: String): Flow<String?> {
        return storageCache.observeEntry(key, chainId).map { it.content }
    }

    override suspend fun observeKeys(keys: List<String>): Flow<Map<String, String?>> {
        return storageCache.observeEntries(keys, chainId).map { it.toMap() }
    }

    override suspend fun observeKeysByPrefix(prefix: String): Flow<Map<String, String?>> {
        return storageCache.observeEntries(prefix, chainId)
            .map { storageEntries ->
                storageEntries.associateBy(
                    keySelector = StorageEntry::storageKey,
                    valueTransform = StorageEntry::content
                )
            }
    }

    private fun List<StorageEntry>.toMap() = associateBy(
        keySelector = StorageEntry::storageKey,
        valueTransform = StorageEntry::content
    )
}
