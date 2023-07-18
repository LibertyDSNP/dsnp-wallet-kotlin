package com.unfinished.account.domain.account.add

import com.unfinished.account.domain.interfaces.AccountRepository
import com.unfinished.account.domain.model.AddAccountType
import com.unfinished.account.data.repository.AddAccountRepository
import com.unfinished.account.domain.advancedEncryption.AdvancedEncryption
import com.unfinished.data.model.ImportJsonMetaData

class AddAccountInteractor(
    private val addAccountRepository: AddAccountRepository,
    private val accountRepository: AccountRepository,
) {

    suspend fun createAccount(
        mnemonic: String,
        advancedEncryption: AdvancedEncryption,
        addAccountType: AddAccountType
    ): Result<Unit> {
        return addAccount(addAccountType) {
            addAccountRepository.addFromMnemonic(
                mnemonic,
                advancedEncryption,
                addAccountType
            )
        }
    }

    suspend fun importFromMnemonic(
        mnemonic: String,
        advancedEncryption: AdvancedEncryption,
        addAccountType: AddAccountType
    ): Result<Unit> {
        return addAccount(addAccountType) {
            addAccountRepository.addFromMnemonic(
                mnemonic,
                advancedEncryption,
                addAccountType
            )
        }
    }

    suspend fun importFromSeed(
        seed: String,
        advancedEncryption: AdvancedEncryption,
        addAccountType: AddAccountType
    ): Result<Unit> {
        return addAccount(addAccountType) {
            addAccountRepository.addFromSeed(
                seed,
                advancedEncryption,
                addAccountType
            )
        }
    }

    suspend fun importFromJson(
        json: String,
        password: String,
        addAccountType: AddAccountType
    ): Result<Unit> {
        return addAccount(addAccountType) {
            addAccountRepository.addFromJson(
                json = json,
                password = password,
                addAccountType = addAccountType
            )
        }
    }

    private suspend inline fun addAccount(addAccountType: AddAccountType, accountInserter: () -> Long) = runCatching {
        val metaId = accountInserter()

        if (addAccountType is AddAccountType.MetaAccount) {
            accountRepository.selectMetaAccount(metaId)
        }
    }

    suspend fun extractJsonMetadata(json: String): Result<ImportJsonMetaData> {
        return runCatching {
            addAccountRepository.extractJsonMetadata(json)
        }
    }
}
