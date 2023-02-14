package com.unfinished.feature_account.domain.interfaces

import io.novafoundation.nova.common.list.GroupedList
import com.unfinished.feature_account.domain.model.LightMetaAccount
import com.unfinished.feature_account.domain.model.MetaAccount
import io.novafoundation.nova.runtime.multiNetwork.chain.model.ChainId
import kotlinx.coroutines.flow.Flow

interface MetaAccountGroupingInteractor {

    fun getMetaAccountsForTransaction(fromId: ChainId, destinationId: ChainId): Flow<GroupedList<LightMetaAccount.Type, MetaAccount>>

    suspend fun hasAvailableMetaAccountsForDestination(fromId: ChainId, destinationId: ChainId): Boolean
}