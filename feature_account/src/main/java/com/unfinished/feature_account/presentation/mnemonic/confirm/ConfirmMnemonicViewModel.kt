package com.unfinished.feature_account.presentation.mnemonic.confirm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.unfinished.feature_account.data.mappers.mapAddAccountPayloadToAddAccountType
import com.unfinished.feature_account.data.mappers.mapAdvancedEncryptionResponseToAdvancedEncryption
import com.unfinished.feature_account.data.mappers.mapOptionalNameToNameChooserState
import io.novafoundation.nova.common.R
import com.unfinished.feature_account.domain.account.add.AddAccountInteractor
import com.unfinished.feature_account.domain.interfaces.AccountInteractor
import com.unfinished.feature_account.presentation.AccountRouter
import com.unfinished.feature_account.presentation.mnemonic.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.novafoundation.nova.common.base.BaseViewModel
import io.novafoundation.nova.common.resources.ResourceManager
import io.novafoundation.nova.common.utils.Event
import io.novafoundation.nova.common.utils.modified
import io.novafoundation.nova.common.utils.sendEvent
import io.novafoundation.nova.common.vibration.DeviceVibrator
import jp.co.soramitsu.fearless_utils.encrypt.junction.BIP32JunctionDecoder
import jp.co.soramitsu.fearless_utils.encrypt.junction.JunctionDecoder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmMnemonicViewModel @Inject constructor(
    private val interactor: AccountInteractor,
    private val addAccountInteractor: AddAccountInteractor,
    private val router: AccountRouter,
    private val deviceVibrator: DeviceVibrator,
    private val resourceManager: ResourceManager,
    private val config: ConfirmMnemonicConfig,
) : BaseViewModel() {

    lateinit var payload: ConfirmMnemonicPayload

    private val originMnemonic by lazy { payload.mnemonic }

    private val shuffledMnemonic by lazy { originMnemonic.shuffled() }

    private var originWordList = listOf<MnemonicWord>()

    private var sourceWordList = listOf<MnemonicWord>()

    private var destinationWordList = listOf<MnemonicWord>()

    private val _sourceWords = MutableStateFlow(initialSourceWords())
    val sourceWords: Flow<List<MnemonicWord>> = _sourceWords

    private val _destinationWords = MutableStateFlow(destinationSourceWords())
    val destinationWords: Flow<List<MnemonicWord>> = _destinationWords

    val nextButtonEnabled = _destinationWords.map {
        originWordList.validateMnemonicPhrase(it)
    }

    val skipVisible = payload.createExtras != null && config.allowShowingSkip

    private val _matchingMnemonicErrorAnimationEvent = MutableLiveData<Event<Unit>>()
    val matchingMnemonicErrorAnimationEvent: LiveData<Event<Unit>> = _matchingMnemonicErrorAnimationEvent

    fun init(payload: ConfirmMnemonicPayload){
        this.payload = payload
    }

    fun homeButtonClicked() {
        router.back()
    }

    fun sourceWordClicked(sourceWord: MnemonicWord) {
        val markedAsRemoved = sourceWord.copy(removed = true)

        val destinationWordsSnapshot = destinationWordList
        val destinationWord = sourceWord.copy(
            indexDisplay = (destinationWordsSnapshot.size + 1).toString()
        )
        val mSourceWords = sourceWordList.modified(markedAsRemoved, markedAsRemoved.byMyId())
        val mDestinationWords = destinationWordsSnapshot.addedNext(mSourceWords,destinationWord)

        sourceWordList = mSourceWords.toMutableList()
        destinationWordList = mDestinationWords.sortByIndexOrder().toMutableList()

        _sourceWords.value = sourceWordList
        _destinationWords.value = destinationWordList
    }

    fun destinationWordClicked(destinationWord: MnemonicWord) {
        val sourceWord = sourceWordList.first { it.content == destinationWord.content }
        val modifiedSourceWord = sourceWord.copy(removed = false)
        val markedAsRemoved = destinationWord.copy(removed = true, content = "")
        val mSourceWords = sourceWordList.modified(modifiedSourceWord, modifiedSourceWord.byMyId())
        val mDestinationWords = destinationWordList.modified(markedAsRemoved, markedAsRemoved.byMyId())

        sourceWordList = mSourceWords.toMutableList()
        destinationWordList = mDestinationWords.sortByIndexOrder().toMutableList()

        _sourceWords.value = sourceWordList
        _destinationWords.value = destinationWordList
    }

    fun reset() {
        _destinationWords.value = emptyList()
        _sourceWords.value = initialSourceWords()
    }

    fun skipClicked() {
        proceed()
    }

    fun continueClicked() {
        if (originWordList.validateMnemonicPhrase(destinationWordList)) {
            proceed()
        } else {
            deviceVibrator.makeShortVibration()
            _matchingMnemonicErrorAnimationEvent.sendEvent()
        }
    }

    private fun initialSourceWords(): List<MnemonicWord> {
        originWordList = originMnemonic.mapIndexed { index, s ->
            MnemonicWord(id = index.twoDigitIndex(), content = s, indexDisplay = "",false)
        }.sortByIndexOrder().toMutableList()

        val list = shuffledMnemonic.mapIndexed { index, s ->
            MnemonicWord(id = index.twoDigitIndex(), content = s, indexDisplay = "", false)
        }.sortByIndexOrder().toMutableList()
        sourceWordList = list
        return list
    }

    private fun destinationSourceWords(): List<MnemonicWord> {
        val list = List(originMnemonic.size) { index ->
            MnemonicWord(id = index.twoDigitIndex(), content = "", indexDisplay = "",true)
        }.sortByIndexOrder().toMutableList()
        destinationWordList = list
        return list
    }

    private fun proceed() {
        val createExtras = payload.createExtras

        if (createExtras != null) {
            createAccount(createExtras)
        } else {
            finishConfirmGame()
        }
    }

    private fun finishConfirmGame() {
        router.back()
    }

    private fun createAccount(extras: ConfirmMnemonicPayload.CreateExtras) {
        viewModelScope.launch {
            val mnemonicString = originMnemonic.joinToString(" ")

            with(extras) {
                val accountNameState = mapOptionalNameToNameChooserState(accountName)
                val addAccountType = mapAddAccountPayloadToAddAccountType(addAccountPayload, accountNameState)
                val advancedEncryption = mapAdvancedEncryptionResponseToAdvancedEncryption(advancedEncryptionPayload)

                addAccountInteractor.createAccount(mnemonicString, advancedEncryption, addAccountType)
                    .onSuccess { continueBasedOnCodeStatus() }
                    .onFailure(::showAccountCreationError)
            }
        }
    }

    private fun showAccountCreationError(throwable: Throwable) {
        val (title, message) = when (throwable) {
            is JunctionDecoder.DecodingError, is BIP32JunctionDecoder.DecodingError -> {
                resourceManager.getString(R.string.account_invalid_derivation_path_title) to
                    resourceManager.getString(R.string.account_invalid_derivation_path_message_v2_2_0)
            }
            else -> {
                resourceManager.getString(R.string.common_error_general_title) to
                    resourceManager.getString(R.string.common_undefined_error_message)
            }
        }

        showError(title, message)
    }

    fun matchingErrorAnimationCompleted() {
        reset()
    }

    private suspend fun continueBasedOnCodeStatus() {
        if (interactor.isCodeSet()) {
            router.openMain()
        } else {
            router.openCreatePincode()
        }
    }
}