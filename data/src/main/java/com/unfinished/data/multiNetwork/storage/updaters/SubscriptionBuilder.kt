package com.unfinished.data.multiNetwork.storage.updaters

import com.unfinished.data.model.storage.StorageChange
import kotlinx.coroutines.flow.Flow

interface SubscriptionBuilder {
    fun subscribe(key: String): Flow<StorageChange>
}
