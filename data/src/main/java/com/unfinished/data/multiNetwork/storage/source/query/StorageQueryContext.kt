package com.unfinished.data.multiNetwork.storage.source.query

import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import jp.co.soramitsu.fearless_utils.runtime.metadata.module.StorageEntry
import kotlinx.coroutines.flow.Flow

typealias DynamicInstanceBinder<V> = (dynamicInstance: Any?) -> V
typealias DynamicInstanceBinderWithKey<K, V> = (dynamicInstance: Any?, key: K) -> V

interface StorageQueryContext {

    val runtime: RuntimeSnapshot

    suspend fun <V> StorageEntry.observe(
        vararg keyArguments: Any?,
        binding: DynamicInstanceBinder<V>
    ): Flow<V>

    suspend fun StorageEntry.entriesRaw(
        vararg prefixArgs: Any?,
    ): Map<String, String?>

    suspend fun StorageEntry.entriesRaw(
        keysArguments: List<List<Any?>>
    ): Map<String, String?>

    suspend fun <V> StorageEntry.query(
        vararg keyArguments: Any?,
        binding: DynamicInstanceBinder<V>
    ): V

    suspend fun StorageEntry.queryRaw(
        vararg keyArguments: Any?
    ): String?
}

fun Collection<*>.wrapSingleArgumentKeys(): List<List<Any?>> = map(::listOf)
