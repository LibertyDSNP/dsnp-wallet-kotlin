package com.unfinished.account.domain.interactor

import com.unfinished.data.util.ext.removed
import com.unfinished.account.domain.interfaces.AccountRepository
import com.unfinished.account.domain.interfaces.MetaAccountGroupingInteractor
import com.unfinished.data.model.LightMetaAccount
import com.unfinished.data.model.MetaAccount
import com.unfinished.data.model.addressIn
import com.unfinished.data.model.hasAccountIn
import com.unfinished.data.multiNetwork.ChainRegistry
import com.unfinished.data.multiNetwork.chain.model.Chain
import com.unfinished.data.multiNetwork.chain.model.ChainId

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
            .removed<MetaAccount> { fromChainAddress == it.addressIn(destination) }
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
