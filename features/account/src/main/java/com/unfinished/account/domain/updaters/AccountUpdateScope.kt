package com.unfinished.account.domain.updaters

import com.unfinished.account.domain.interfaces.AccountRepository
import com.unfinished.data.model.account.MetaAccount
import com.unfinished.data.multiNetwork.storage.updaters.UpdateScope
import kotlinx.coroutines.flow.Flow

class AccountUpdateScope(
    private val accountRepository: AccountRepository
) : UpdateScope {

    override fun invalidationFlow(): Flow<MetaAccount> {
        return accountRepository.selectedMetaAccountFlow()
    }

    suspend fun getAccount() = accountRepository.getSelectedMetaAccount()
}
