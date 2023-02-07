package com.unfinished.feature_account.domain.interactor

import io.novafoundation.nova.common.list.GroupedList
import io.novafoundation.nova.common.utils.flowOf
import io.novafoundation.nova.common.utils.removed
import com.unfinished.feature_account.domain.interfaces.AccountRepository
import com.unfinished.feature_account.domain.interfaces.MetaAccountGroupingInteractor
import com.unfinished.feature_account.domain.model.LightMetaAccount
import com.unfinished.feature_account.domain.model.MetaAccount
import com.unfinished.feature_account.domain.model.addressIn
import com.unfinished.feature_account.domain.model.hasAccountIn
import io.novafoundation.nova.runtime.multiNetwork.ChainRegistry
import io.novafoundation.nova.runtime.multiNetwork.chain.model.Chain
import io.novafoundation.nova.runtime.multiNetwork.chain.model.ChainId
import kotlinx.coroutines.flow.Flow

class MetaAccountGroupingInteractorImpl(
    private val chainRegistry: ChainRegistry,
    private val accountRepository: AccountRepository,
) : MetaAccountGroupingInteractor {

    override fun getMetaAccountsForTransaction(fromId: ChainId, destinationId: ChainId): Flow<GroupedList<LightMetaAccount.Type, MetaAccount>> = flowOf {
        val fromChain = chainRegistry.getChain(fromId)
        val destinationChain = chainRegistry.getChain(destinationId)
        getValidMetaAccountsForTransaction(fromChain, destinationChain)
            .groupBy(MetaAccount::type)
            .toSortedMap(metaAccountTypeComparator())
    }

    override suspend fun hasAvailableMetaAccountsForDestination(fromId: ChainId, destinationId: ChainId): Boolean {
        val fromChain = chainRegistry.getChain(fromId)
        val destinationChain = chainRegistry.getChain(destinationId)
        return getValidMetaAccountsForTransaction(fromChain, destinationChain)
            .any { it.hasAccountIn(destinationChain) }
    }

    private suspend fun getValidMetaAccountsForTransaction(from: Chain, destination: Chain): List<MetaAccount> {
        val selectedMetaAccount = accountRepository.getSelectedMetaAccount()
        val fromChainAddress = selectedMetaAccount.addressIn(from)
        return accountRepository.allMetaAccounts()
            .removed { fromChainAddress == it.addressIn(destination) }
            .filter { it.type != LightMetaAccount.Type.WATCH_ONLY }
    }

    private fun metaAccountTypeComparator() = compareBy<LightMetaAccount.Type> {
        when (it) {
            LightMetaAccount.Type.SECRETS -> 0
            LightMetaAccount.Type.PARITY_SIGNER -> 1
            LightMetaAccount.Type.LEDGER -> 2
            LightMetaAccount.Type.WATCH_ONLY -> 3
        }
    }
}
