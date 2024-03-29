package com.unfinished.runtime.multiNetwork.connection.autobalance.strategy

import com.unfinished.runtime.multiNetwork.chain.model.ChainId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class AutoBalanceStrategyProvider {

    private val autoBalanceStrategy = RoundRobinStrategy()

    // TODO take user settings into account (tbd in the Networks screen task scope)
    fun strategyFlowFor(chainId: ChainId): Flow<AutoBalanceStrategy> {
        return flowOf(autoBalanceStrategy)
    }
}
