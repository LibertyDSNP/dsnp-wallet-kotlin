package com.unfinished.runtime.storage.source.query

import com.unfinished.runtime.network.runtime.binding.BlockHash
import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.fromHex
import jp.co.soramitsu.fearless_utils.runtime.metadata.module.StorageEntry
import jp.co.soramitsu.fearless_utils.runtime.metadata.storageKey
import jp.co.soramitsu.fearless_utils.runtime.metadata.storageKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

abstract class BaseStorageQueryContext(
    override val runtime: RuntimeSnapshot,
    private val at: BlockHash?,
) : StorageQueryContext {

    protected abstract suspend fun queryKeysByPrefix(prefix: String, at: BlockHash?): List<String>

    protected abstract suspend fun queryEntriesByPrefix(prefix: String, at: BlockHash?): Map<String, String?>

    protected abstract suspend fun queryKeys(keys: List<String>, at: BlockHash?): Map<String, String?>

    protected abstract suspend fun queryKey(key: String, at: BlockHash?): String?

    protected abstract suspend fun observeKey(key: String): Flow<String?>

    protected abstract suspend fun observeKeys(keys: List<String>): Flow<Map<String, String?>>

    protected abstract suspend fun observeKeysByPrefix(prefix: String): Flow<Map<String, String?>>

    override suspend fun StorageEntry.entriesRaw(vararg prefixArgs: Any?): Map<String, String?> {
        return queryEntriesByPrefix(storageKey(runtime, *prefixArgs), at)
    }

    override suspend fun StorageEntry.entriesRaw(keysArguments: List<List<Any?>>): Map<String, String?> {
        return queryKeys(storageKeys(runtime, keysArguments), at)
    }

    override suspend fun <V> StorageEntry.query(
        vararg keyArguments: Any?,
        binding: DynamicInstanceBinder<V>
    ): V {
        val storageKey = storageKeyWith(keyArguments)
        val scaleResult = queryKey(storageKey, at)
        val decoded = scaleResult?.let { type.value?.fromHex(runtime, scaleResult) }

        return binding(decoded)
    }

    override suspend fun StorageEntry.queryRaw(vararg keyArguments: Any?): String? {
        val storageKey = storageKeyWith(keyArguments)

        return queryKey(storageKey, at)
    }

    override suspend fun <V> StorageEntry.observe(
        vararg keyArguments: Any?,
        binding: DynamicInstanceBinder<V>
    ): Flow<V> {
        val storageKey = storageKeyWith(keyArguments)

        return observeKey(storageKey).map { scale ->
            val dynamicInstance = scale?.let {
                type.value?.fromHex(runtime, scale)
            }

            binding(dynamicInstance)
        }
    }

    private fun StorageEntry.storageKeyWith(keyArguments: Array<out Any?>): String {
        return if (keyArguments.isEmpty()) {
            storageKey()
        } else {
            storageKey(runtime, *keyArguments)
        }
    }
}
