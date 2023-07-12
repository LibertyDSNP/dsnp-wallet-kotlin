package com.unfinished.runtime.repository

import com.unfinished.runtime.network.runtime.binding.bindNumber
import com.unfinished.runtime.util.timestamp
import com.unfinished.runtime.multiNetwork.chain.model.ChainId
import com.unfinished.runtime.storage.source.StorageDataSource
import jp.co.soramitsu.fearless_utils.runtime.metadata.storage
import java.math.BigInteger

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
