package com.unfinished.feature_account.presentation.mnemonic.backup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.novafoundation.nova.common.R
import com.unfinished.feature_account.domain.account.export.mnemonic.ExportMnemonicInteractor
import com.unfinished.feature_account.domain.interactor.AdvancedEncryptionInteractor
import com.unfinished.feature_account.domain.interfaces.AccountInteractor
import com.unfinished.feature_account.presentation.AccountRouter
import com.unfinished.feature_account.presentation.AdvancedEncryptionCommunicator
import com.unfinished.feature_account.presentation.AdvancedEncryptionRequester
import com.unfinished.feature_account.presentation.lastResponseOrDefault
import com.unfinished.feature_account.presentation.model.account.AdvancedEncryptionPayload
import io.novafoundation.nova.common.base.BaseViewModel
import io.novafoundation.nova.common.resources.ResourceManager
import io.novafoundation.nova.common.utils.Event
import io.novafoundation.nova.common.utils.flowOf
import io.novafoundation.nova.common.utils.inBackground
import io.novafoundation.nova.common.utils.sendEvent
import com.unfinished.feature_account.presentation.mnemonic.confirm.ConfirmMnemonicPayload
import com.unfinished.feature_account.presentation.mnemonic.confirm.ConfirmMnemonicPayload.CreateExtras
import com.unfinished.feature_account.presentation.mnemonic.confirm.MnemonicWord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BackupMnemonicViewModel @Inject constructor(
    private val interactor: AccountInteractor,
    private val exportMnemonicInteractor: ExportMnemonicInteractor,
    private val router: AccountRouter,
    private val advancedEncryptionInteractor: AdvancedEncryptionInteractor,
    private val resourceManager: ResourceManager,
    private val advancedEncryptionCommunicator: AdvancedEncryptionCommunicator
) : BaseViewModel() {

    lateinit var payload: BackupMnemonicPayload

    private val mnemonicFlow by lazy {
        flowOf {
            when (payload) {
                is BackupMnemonicPayload.Confirm -> exportMnemonicInteractor.getMnemonic(
                    (payload as BackupMnemonicPayload.Confirm).metaAccountId,
                    (payload as BackupMnemonicPayload.Confirm).chainId
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
            MnemonicWord(id = index.toString(), content = s, indexDisplay = "", false)
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

    fun init(payload: BackupMnemonicPayload) {
        this.payload = payload
    }

    fun showWarningDialog() {
        _showMnemonicWarningDialog.sendEvent()
    }

    fun homeButtonClicked() {
        router.back()
    }

    fun optionsClicked() {
        val advancedEncryptionPayload = when (payload) {
            is BackupMnemonicPayload.Confirm -> AdvancedEncryptionPayload.View(
                (payload as BackupMnemonicPayload.Confirm).metaAccountId,
                (payload as BackupMnemonicPayload.Confirm).chainId
            )
            is BackupMnemonicPayload.Create -> AdvancedEncryptionPayload.Change((payload as BackupMnemonicPayload.Create).addAccountPayload)
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

    fun List<MnemonicWord>.reArrangeWords(): List<MnemonicWord> {
        var oddIndex = 1
        var evenIndex = 7
        val words = arrayListOf<MnemonicWord>()
        forEachIndexed { index, _ ->
            if (index == 0 || index % 2 == 0) {
                this[oddIndex - 1].indexDisplay = "0$oddIndex"
                words.add(this[oddIndex - 1])
                oddIndex += 1
            } else {
                this[evenIndex - 1].indexDisplay = if (evenIndex >= 10)
                    "$evenIndex" else "0$evenIndex"
                words.add(this[evenIndex - 1])
                evenIndex += 1
            }
        }
        return words
    }
}
