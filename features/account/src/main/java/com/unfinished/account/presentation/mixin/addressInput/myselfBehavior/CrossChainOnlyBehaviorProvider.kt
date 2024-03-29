package com.unfinished.account.presentation.mixin.addressInput.myselfBehavior

import com.unfinished.data.util.invoke
import com.unfinished.account.domain.interfaces.SelectedAccountUseCase
import com.unfinished.account.domain.model.addressIn
import com.unfinished.account.domain.model.hasAccountIn
import com.unfinished.runtime.multiNetwork.chain.model.Chain
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CrossChainOnlyBehaviorProvider(
    private val accountUseCase: SelectedAccountUseCase,
    private val originChain: Deferred<Chain>,
    destinationChain: Flow<Chain>,
) : MyselfBehaviorProvider {

    override val behavior: Flow<MyselfBehavior> = destinationChain.map { it ->
        Behavior(originChain = originChain(), destinationChain = it)
    }

    private inner class Behavior(
        private val originChain: Chain,
        private val destinationChain: Chain
    ) : MyselfBehavior {

        override suspend fun myselfAvailable(): Boolean {
            val metaAccount = accountUseCase.getSelectedMetaAccount()

            return originChain.id != destinationChain.id && metaAccount.hasAccountIn(destinationChain)
        }

        override suspend fun myself(): String? {
            val metaAccount = accountUseCase.getSelectedMetaAccount()

            return metaAccount.addressIn(destinationChain)
        }
    }
}
