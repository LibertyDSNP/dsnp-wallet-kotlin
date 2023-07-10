package com.unfinished.feature_account.presentation.mnemonic.backup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.unfinished.common.R
import com.unfinished.feature_account.domain.account.export.mnemonic.ExportMnemonicInteractor
import com.unfinished.feature_account.domain.interactor.AdvancedEncryptionInteractor
import com.unfinished.feature_account.domain.interfaces.AccountInteractor
import com.unfinished.feature_account.presentation.AccountRouter
import com.unfinished.feature_account.presentation.AdvancedEncryptionCommunicator
import com.unfinished.feature_account.presentation.AdvancedEncryptionRequester
import com.unfinished.feature_account.presentation.lastResponseOrDefault
import com.unfinished.feature_account.presentation.model.account.AdvancedEncryptionPayload
import com.unfinished.common.base.BaseViewModel
import com.unfinished.common.resources.ResourceManager
import com.unfinished.common.utils.Event
import com.unfinished.common.utils.flowOf
import com.unfinished.common.utils.inBackground
import com.unfinished.common.utils.sendEvent
import com.unfinished.feature_account.presentation.mnemonic.confirm.ConfirmMnemonicPayload
import com.unfinished.feature_account.presentation.mnemonic.confirm.ConfirmMnemonicPayload.CreateExtras
import com.unfinished.feature_account.presentation.mnemonic.confirm.MnemonicWord
import com.unfinished.feature_account.presentation.mnemonic.reArrangeWords
import com.unfinished.feature_account.presentation.mnemonic.twoDigitIndex
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class BackupMnemonicViewModel @AssistedInject constructor(
    private val interactor: AccountInteractor,
    private val exportMnemonicInteractor: ExportMnemonicInteractor,
    private val router: AccountRouter,
    private val advancedEncryptionInteractor: AdvancedEncryptionInteractor,
    private val resourceManager: ResourceManager,
    private val advancedEncryptionCommunicator: AdvancedEncryptionCommunicator,
    @Assisted private val payload: BackupMnemonicPayload
) : BaseViewModel() {

    private val mnemonicFlow by lazy {
        flowOf {
            when (payload) {
                is BackupMnemonicPayload.Confirm -> exportMnemonicInteractor.getMnemonic(
                    payload.metaAccountId,
                    payload.chainId
                )
                is BackupMnemonicPayload.Create -> interactor.generateMnemonic()
            }
        }
            .inBackground()
            .share()
    }


    private val _showMnemonicWarningDialog = MutableLiveData<Event<Unit>>()
    val showMnemonicWarningDialog: LiveData<Event<Unit>> = _showMnemonicWarningDialog

    private val warningAcceptedFlow = MutableStateFlow(false)

    val mnemonicDisplay = combine(
        mnemonicFlow,
        warningAcceptedFlow
    ) { mnemonic, warningAccepted ->
        mnemonic.wordList.mapIndexed { index, s ->
            MnemonicWord(id = index.twoDigitIndex(), content = s, indexDisplay = "", false)
        }.reArrangeWords().takeIf { warningAccepted }
    }

    val continueText = flowOf {
        val stringRes = when (payload) {
            is BackupMnemonicPayload.Confirm -> R.string.account_confirm_mnemonic
            is BackupMnemonicPayload.Create -> com.unfinished.feature_account.R.string.seed_phrase_btn
        }

        resourceManager.getString(stringRes)
    }
        .inBackground()
        .share()

    fun showWarningDialog() {
        _showMnemonicWarningDialog.sendEvent()
    }

    fun homeButtonClicked() {
        router.back()
    }

    fun optionsClicked() {
        val advancedEncryptionPayload = when (payload) {
            is BackupMnemonicPayload.Confirm -> AdvancedEncryptionPayload.View(
                payload.metaAccountId,
                payload.chainId
            )
            is BackupMnemonicPayload.Create -> AdvancedEncryptionPayload.Change((payload).addAccountPayload)
        }

        advancedEncryptionCommunicator.openRequest(advancedEncryptionPayload)
    }

    fun warningAccepted() {
        warningAcceptedFlow.value = true
    }

    fun warningDeclined() {
        router.back()
    }

    fun nextClicked() = launch {
        val createExtras = (payload as? BackupMnemonicPayload.Create)?.let {
            val advancedEncryptionResponse = advancedEncryptionCommunicator.lastResponseOrDefault(
                it.addAccountPayload,
                advancedEncryptionInteractor
            )

            CreateExtras(
                accountName = it.newWalletName,
                addAccountPayload = it.addAccountPayload,
                advancedEncryptionPayload = advancedEncryptionResponse
            )
        }

        val payload = ConfirmMnemonicPayload(
            mnemonic = mnemonicFlow.first().wordList,
            createExtras = createExtras
        )

        router.openConfirmMnemonicOnCreate(payload)
    }

    companion object {
        fun provideFactory(
            assistedFactory: AssistedFactory,
            payload: BackupMnemonicPayload
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.injectPayload(payload) as T
            }
        }
    }

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun injectPayload(payload: BackupMnemonicPayload): BackupMnemonicViewModel
    }
}