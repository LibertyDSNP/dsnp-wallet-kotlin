package com.unfinished.dsnp_wallet_kotlin.usecase

import com.unfinished.feature_account.data.mappers.mapAdvancedEncryptionResponseToAdvancedEncryption
import com.unfinished.feature_account.domain.account.add.AddAccountInteractor
import com.unfinished.feature_account.domain.account.export.mnemonic.ExportMnemonicInteractor
import com.unfinished.feature_account.domain.interactor.AdvancedEncryptionInteractor
import com.unfinished.feature_account.domain.interfaces.AccountInteractor
import com.unfinished.feature_account.domain.model.AddAccountType
import com.unfinished.feature_account.presentation.AdvancedEncryptionCommunicator
import com.unfinished.feature_account.presentation.lastResponseOrDefault
import com.unfinished.feature_account.presentation.model.account.add.AddAccountPayload
import jp.co.soramitsu.fearless_utils.encrypt.mnemonic.Mnemonic
import javax.inject.Inject

class AccountUseCase @Inject constructor(
    private val accountInteractor: AccountInteractor,
    private val exportMnemonicInteractor: ExportMnemonicInteractor,
    private val advancedEncryptionInteractor: AdvancedEncryptionInteractor,
    private val addAccountInteractor: AddAccountInteractor,
    private val advancedEncryptionCommunicator: AdvancedEncryptionCommunicator
) {

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
}