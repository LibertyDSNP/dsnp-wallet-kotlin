package com.unfinished.data.repository

import com.unfinished.data.multiNetwork.runtime.binding.bindNumber
import com.unfinished.data.util.ext.timestamp
import com.unfinished.data.multiNetwork.chain.model.ChainId
import com.unfinished.data.multiNetwork.storage.source.StorageDataSource
import jp.co.soramitsu.fearless_utils.runtime.metadata.storage
import java.math.BigInteger

/*
    TODO: It's not using but leave it incase we need it in future
 */

typealias UnixTime = BigInteger

interface TimestampRepository {

    suspend fun now(chainId: ChainId): UnixTime
}

class RemoteTimestampRepository(
    private val remoteStorageDataSource: StorageDataSource
) : TimestampRepository {

    override suspend fun now(chainId: ChainId): UnixTime {
        return remoteStorageDataSource.query(chainId) {
            runtime.metadata.timestamp().storage("Now").query(binding = ::bindNumber)
        }
    }
}
