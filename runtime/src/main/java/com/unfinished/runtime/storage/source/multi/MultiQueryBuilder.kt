package com.unfinished.runtime.storage.source.multi

import com.unfinished.runtime.storage.source.query.wrapSingleArgumentKeys
import jp.co.soramitsu.fearless_utils.runtime.metadata.module.StorageEntry

interface MultiQueryBuilder {

    fun StorageEntry.queryKey(vararg args: Any?)

    fun StorageEntry.queryKeys(keysArgs: List<List<Any?>>)

    fun StorageEntry.querySingleArgKeys(singleArgKeys: List<Any?>) = queryKeys(singleArgKeys.wrapSingleArgumentKeys())
}
