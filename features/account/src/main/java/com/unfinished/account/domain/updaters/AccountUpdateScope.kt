package com.unfinished.account.domain.updaters

import com.unfinished.runtime.network.updaters.UpdateScope
import com.unfinished.account.domain.interfaces.AccountRepository
import com.unfinished.account.domain.model.MetaAccount
import kotlinx.coroutines.flow.Flow

class AccountUpdateScope(
    private val accountRepository: AccountRepository
) : UpdateScope {

    override fun invalidationFlow(): Flow<MetaAccount> {
        return accountRepository.selectedMetaAccountFlow()
    }

    suspend fun getAccount() = accountRepository.getSelectedMetaAccount()
}
