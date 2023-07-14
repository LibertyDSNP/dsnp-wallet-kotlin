package com.unfinished.dsnp_wallet_kotlin.usecase

import com.unfinished.account.data.mappers.mapAdvancedEncryptionResponseToAdvancedEncryption
import com.unfinished.account.domain.account.add.AddAccountInteractor
import com.unfinished.account.domain.account.export.mnemonic.ExportMnemonicInteractor
import com.unfinished.account.domain.interactor.AdvancedEncryptionInteractor
import com.unfinished.account.domain.interfaces.AccountInteractor
import com.unfinished.account.domain.model.AddAccountType
import com.unfinished.account.presentation.AdvancedEncryptionCommunicator
import com.unfinished.account.presentation.lastResponseOrDefault
import com.unfinished.account.presentation.model.account.add.AddAccountPayload
import jp.co.soramitsu.fearless_utils.encrypt.mnemonic.Mnemonic
import javax.inject.Inject
import javax.inject.Singleton

class AccountUseCase @Inject constructor(
    private val accountInteractor: AccountInteractor,
    private val exportMnemonicInteractor: ExportMnemonicInteractor,
    private val advancedEncryptionInteractor: AdvancedEncryptionInteractor,
    private val addAccountInteractor: AddAccountInteractor,
    private val advancedEncryptionCommunicator: AdvancedEncryptionCommunicator
) {
    /**
     * This is temp and will be removed once we implement fetching a user's account
     */
    private companion object {
        var tempHandle: String = "neverendingwinter"
    }

    suspend fun fetchMnemoic(): Mnemonic {
        /**
         * Figure out if we need to generate a new mnemoic vs fetching an existing one using
         * ExportMnemonicInteractor.getMnemonic()
         */
        return accountInteractor.generateMnemonic()
    }

    suspend fun createAccount(
        mnemonicString: String,
        addAccountPayload: AddAccountPayload
    ): Result<Unit> {
        val addAccountType = when (addAccountPayload) {
            is AddAccountPayload.MetaAccount -> AddAccountType.MetaAccount(name = addAccountPayload.accountName)
            is AddAccountPayload.ChainAccount -> AddAccountType.ChainAccount(
                chainId = addAccountPayload.chainId,
                metaId = addAccountPayload.metaId
            )
        }

        val advancedEncryption = advancedEncryptionCommunicator.lastResponseOrDefault(
            addAccountPayload,
            advancedEncryptionInteractor
        ).let {
            mapAdvancedEncryptionResponseToAdvancedEncryption(it)
        }

        return addAccountInteractor.createAccount(
            mnemonicString,
            advancedEncryption,
            addAccountType
        )
    }

    suspend fun createHandle(handle: String) {
        tempHandle = handle
    }

    suspend fun fetchHandle(): String = "$tempHandle.23"
}