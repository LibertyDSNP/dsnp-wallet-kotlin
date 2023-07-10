package com.unfinished.feature_account.domain.interfaces

import com.unfinished.common.list.GroupedList
import com.unfinished.feature_account.domain.model.LightMetaAccount
import com.unfinished.feature_account.domain.model.MetaAccount
import com.unfinished.runtime.multiNetwork.chain.model.ChainId
import kotlinx.coroutines.flow.Flow

interface MetaAccountGroupingInteractor {

    fun getMetaAccountsForTransaction(fromId: ChainId, destinationId: ChainId): Flow<GroupedList<LightMetaAccount.Type, MetaAccount>>

    suspend fun hasAvailableMetaAccountsForDestination(fromId: ChainId, destinationId: ChainId): Boolean
}
