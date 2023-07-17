package com.unfinished.account.presentation.importing.source

import com.unfinished.account.domain.account.add.AddAccountInteractor
import com.unfinished.account.domain.interactor.AdvancedEncryptionInteractor
import com.unfinished.account.presentation.AdvancedEncryptionRequester
import com.unfinished.common.resources.ClipboardManager
import com.unfinished.account.presentation.importing.FileReader
import com.unfinished.account.presentation.mixin.AccountNameChooserMixin
import com.unfinished.account.presentation.model.account.add.AddAccountPayload
import com.unfinished.account.presentation.model.account.add.SecretType
import com.unfinished.account.presentation.importing.source.model.ImportSource
import com.unfinished.account.presentation.importing.source.model.JsonImportSource
import com.unfinished.account.presentation.importing.source.model.MnemonicImportSource
import com.unfinished.account.presentation.importing.source.model.RawSeedImportSource
import kotlinx.coroutines.CoroutineScope

class ImportSourceFactory(
    private val addAccountInteractor: AddAccountInteractor,
    private val clipboardManager: ClipboardManager,
    private val advancedEncryptionInteractor: AdvancedEncryptionInteractor,
    private val advancedEncryptionRequester: AdvancedEncryptionRequester,
    private val fileReader: FileReader,
) {

    fun create(
        secretType: SecretType,
        scope: CoroutineScope,
        payload: AddAccountPayload,
        accountNameChooserMixin: AccountNameChooserMixin,
    ): ImportSource {
        return when (secretType) {
            SecretType.MNEMONIC -> MnemonicImportSource(
                addAccountInteractor = addAccountInteractor,
                addAccountPayload = payload,
                advancedEncryptionInteractor = advancedEncryptionInteractor,
                advancedEncryptionCommunicator = advancedEncryptionRequester
            )
            SecretType.SEED -> RawSeedImportSource(
                addAccountInteractor = addAccountInteractor,
                addAccountPayload = payload,
                advancedEncryptionInteractor = advancedEncryptionInteractor,
                advancedEncryptionCommunicator = advancedEncryptionRequester
            )
            SecretType.JSON -> JsonImportSource(
                accountNameChooserMixin = accountNameChooserMixin,
                addAccountInteractor = addAccountInteractor,
                clipboardManager = clipboardManager,
                fileReader = fileReader,
                scope = scope,
                payload = payload
            )
        }
    }
}
