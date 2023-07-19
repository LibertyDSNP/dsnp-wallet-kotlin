package com.unfinished.data.repository

import com.unfinished.data.multiNetwork.runtime.binding.bindNumber
import com.unfinished.data.util.ext.balances
import com.unfinished.data.multiNetwork.chain.model.ChainId
import com.unfinished.data.multiNetwork.storage.source.StorageDataSource
import jp.co.soramitsu.fearless_utils.runtime.metadata.storage
import java.math.BigInteger

/*
    TODO: It's not using but leave it incase we need it in future
 */
interface TotalIssuanceRepository {

    suspend fun getTotalIssuance(chainId: ChainId): BigInteger
}

internal class RealTotalIssuanceRepository(
    private val storageDataSource: StorageDataSource
) : TotalIssuanceRepository {

    override suspend fun getTotalIssuance(chainId: ChainId): BigInteger {
        return storageDataSource.query(chainId) {
            runtime.metadata.balances().storage("TotalIssuance").query(binding = ::bindNumber)
        }
    }
}
