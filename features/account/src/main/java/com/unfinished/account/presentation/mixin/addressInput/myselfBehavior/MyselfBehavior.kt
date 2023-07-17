package com.unfinished.account.presentation.mixin.addressInput.myselfBehavior

import kotlinx.coroutines.flow.Flow

interface MyselfBehaviorProvider {

    val behavior: Flow<MyselfBehavior>
}

interface MyselfBehavior {

    suspend fun myselfAvailable(): Boolean
    suspend fun myself(): String?
}
