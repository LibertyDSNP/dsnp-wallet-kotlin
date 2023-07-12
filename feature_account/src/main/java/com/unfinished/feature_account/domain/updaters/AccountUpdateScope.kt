package com.unfinished.feature_account.domain.updaters

import com.unfinished.data.api.updater.UpdateScope
import com.unfinished.feature_account.domain.interfaces.AccountRepository
import com.unfinished.feature_account.domain.model.MetaAccount
import kotlinx.coroutines.flow.Flow

class AccountUpdateScope(
    private val accountRepository: AccountRepository
) : UpdateScope {

    override fun invalidationFlow(): Flow<MetaAccount> {
        return accountRepository.selectedMetaAccountFlow()
    }

    suspend fun getAccount() = accountRepository.getSelectedMetaAccount()
}
