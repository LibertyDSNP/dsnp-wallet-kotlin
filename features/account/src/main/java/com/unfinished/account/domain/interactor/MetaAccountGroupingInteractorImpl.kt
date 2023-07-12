package com.unfinished.account.domain.interactor

import com.unfinished.data.util.removed
import com.unfinished.account.domain.interfaces.AccountRepository
import com.unfinished.account.domain.interfaces.MetaAccountGroupingInteractor
import com.unfinished.account.domain.model.LightMetaAccount
import com.unfinished.account.domain.model.MetaAccount
import com.unfinished.account.domain.model.addressIn
import com.unfinished.account.domain.model.hasAccountIn
import com.unfinished.runtime.multiNetwork.ChainRegistry
import com.unfinished.runtime.multiNetwork.chain.model.Chain
import com.unfinished.runtime.multiNetwork.chain.model.ChainId

class MetaAccountGroupingInteractorImpl(
    private val chainRegistry: ChainRegistry,
    private val accountRepository: AccountRepository,
) : MetaAccountGroupingInteractor {

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
