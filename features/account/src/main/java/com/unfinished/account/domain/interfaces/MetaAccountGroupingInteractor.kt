package com.unfinished.account.domain.interfaces

import com.unfinished.runtime.multiNetwork.chain.model.ChainId

interface MetaAccountGroupingInteractor {
    suspend fun hasAvailableMetaAccountsForDestination(fromId: ChainId, destinationId: ChainId): Boolean
}
