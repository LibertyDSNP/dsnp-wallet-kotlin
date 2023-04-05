package com.unfinished.feature_account.presentation.importing

import android.content.Intent
import io.novafoundation.nova.common.R
import com.unfinished.feature_account.data.mappers.mapAddAccountPayloadToAddAccountType
import com.unfinished.feature_account.domain.interfaces.AccountAlreadyExistsException
import com.unfinished.feature_account.domain.interfaces.AccountInteractor
import com.unfinished.feature_account.presentation.AccountRouter
import com.unfinished.feature_account.presentation.AdvancedEncryptionRequester
import com.unfinished.feature_account.presentation.mixin.AccountNameChooserMixin
import com.unfinished.feature_account.presentation.mixin.WithAccountNameChooserMixin
import com.unfinished.feature_account.presentation.model.account.AdvancedEncryptionPayload
import com.unfinished.feature_account.presentation.model.account.add.ImportAccountPayload
import io.novafoundation.nova.common.base.BaseViewModel
import io.novafoundation.nova.common.mixin.MixinFactory
import io.novafoundation.nova.common.resources.ResourceManager
import io.novafoundation.nova.common.utils.withFlagSet
import io.novafoundation.nova.common.view.ButtonState
import com.unfinished.feature_account.presentation.importing.source.ImportSourceFactory
import com.unfinished.feature_account.presentation.importing.source.model.FileRequester
import com.unfinished.feature_account.presentation.importing.source.model.ImportError
import jp.co.soramitsu.fearless_utils.encrypt.junction.BIP32JunctionDecoder
import jp.co.soramitsu.fearless_utils.encrypt.junction.JunctionDecoder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class ImportAccountViewModel(
    private val interactor: AccountInteractor,
    private val router: AccountRouter,
    private val resourceManager: ResourceManager,
    accountNameChooserFactory: MixinFactory<AccountNameChooserMixin.Presentation>,
    private val advancedEncryptionRequester: AdvancedEncryptionRequester,
    private val payload: ImportAccountPayload,
    importSourceFactory: ImportSourceFactory,
) : BaseViewModel(),
    WithAccountNameChooserMixin {

    override val accountNameChooser: AccountNameChooserMixin.Presentation = accountNameChooserFactory.create(scope = this)

    val importSource = importSourceFactory.create(
        secretType = payload.type,
        scope = this,
        payload = payload.addAccountPayload,
        accountNameChooserMixin = accountNameChooser
    )

    private val importInProgressFlow = MutableStateFlow(false)

    private val nextButtonEnabledFlow = combine(
        importSource.fieldsValidFlow,
        accountNameChooser.nameValid,
    ) { fieldsValid, nameValid -> fieldsValid and nameValid }

    val nextButtonState = nextButtonEnabledFlow.combine(importInProgressFlow) { enabled, inProgress ->
        when {
            inProgress -> ButtonState.PROGRESS
            enabled -> ButtonState.NORMAL
            else -> ButtonState.DISABLED
        }
    }

    fun homeButtonClicked() {
        router.back()
    }

    fun optionsClicked() {
        advancedEncryptionRequester.openRequest(AdvancedEncryptionPayload.Change(payload.addAccountPayload))
    }

    fun nextClicked() = launch {
        importInProgressFlow.withFlagSet {
            val nameState = accountNameChooser.nameState.value!!
            val addAccountType = mapAddAccountPayloadToAddAccountType(payload.addAccountPayload, nameState)

            importSource.performImport(addAccountType)
                .onSuccess { continueBasedOnCodeStatus() }
                .onFailure(::handleCreateAccountError)
        }
    }

    fun systemCallResultReceived(requestCode: Int, intent: Intent) {
        if (importSource is FileRequester) {
            val currentRequestCode = importSource.chooseJsonFileEvent.value!!.peekContent()

            if (requestCode == currentRequestCode) {
                importSource.fileChosen(intent.data!!)
            }
        }
    }

    private suspend fun continueBasedOnCodeStatus() {
        if (interactor.isCodeSet()) {
            router.openMain()
        } else {
            router.openCreatePincode()
        }
    }

    private fun handleCreateAccountError(throwable: Throwable) {
        var errorMessage = importSource.handleError(throwable)

        if (errorMessage == null) {
            errorMessage = when (throwable) {
                is AccountAlreadyExistsException -> ImportError(
                    titleRes = R.string.account_add_already_exists_message,
                    messageRes = R.string.account_error_try_another_one
                )
                is JunctionDecoder.DecodingError, is BIP32JunctionDecoder.DecodingError -> ImportError(
                    titleRes = R.string.account_invalid_derivation_path_title,
                    messageRes = R.string.account_invalid_derivation_path_message_v2_2_0
                )
                else -> ImportError()
            }
        }

        val title = resourceManager.getString(errorMessage.titleRes)
        val message = resourceManager.getString(errorMessage.messageRes)

        showError(title, message)
    }
}
