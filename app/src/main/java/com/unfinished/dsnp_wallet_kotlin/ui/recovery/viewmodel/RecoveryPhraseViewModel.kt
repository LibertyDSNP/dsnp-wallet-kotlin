package com.unfinished.dsnp_wallet_kotlin.ui.recovery.viewmodel

import androidx.lifecycle.viewModelScope
import com.unfinished.dsnp_wallet_kotlin.ui.recovery.uimodel.RecoveryPhraseUiModel
import com.unfinished.dsnp_wallet_kotlin.ui.recovery.uimodel.SeedKey
import com.unfinished.dsnp_wallet_kotlin.ui.recovery.uimodel.SeedKeyState
import com.unfinished.dsnp_wallet_kotlin.usecase.AccountUseCase
import com.unfinished.uikit.UiState
import com.unfinished.uikit.toDataLoaded
import dagger.hilt.android.lifecycle.HiltViewModel
import com.unfinished.common.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecoveryPhraseViewModel @Inject constructor(
    private val accountUseCase: AccountUseCase
) : BaseViewModel() {

    private companion object {
        const val numberFormat = "%02d"
    }

    private val _uiStateFLow = MutableStateFlow<UiState<RecoveryPhraseUiModel>>(UiState.Loading())
    val uiStateFLow = _uiStateFLow.asStateFlow()

    init {
        viewModelScope.launch {
            /**
             * TODO: Check if we already have an account and use
             * ExportMnemonicInteractor.getMnemonic()
             */
            val mNemonic = accountUseCase.fetchMnemoic()

            _uiStateFLow.value = RecoveryPhraseUiModel(
                seedKeys = mNemonic.wordList.mapIndexed { index, word ->
                    SeedKey(prefix = String.format(numberFormat, index + 1), key = word)
                }
            ).toDataLoaded()
        }

    }

    fun shuffleSeedKeys() {
        (_uiStateFLow.value as? UiState.DataLoaded)?.data?.let {
            _uiStateFLow.value = it.copy(
                currentRandomSeedKeys = it.seedKeys.shuffled(),
                seedKeyState = SeedKeyState.Init
            ).toDataLoaded()
        }
    }

    fun addSeedKeyToGuess(seedKey: SeedKey) {
        (_uiStateFLow.value as? UiState.DataLoaded)?.data?.let {
            _uiStateFLow.value = it.copy(
                seedKeyState = SeedKeyState.Init,
                currentSeedGuesses = it.currentSeedGuesses.toMutableList().apply {
                    val index = indexOfFirst { index -> index == null }

                    if (index > -1) {
                        removeAt(index)
                        add(index, seedKey)
                    }
                }
            ).toDataLoaded()
        }
    }

    fun removeSeedKeyFromGuess(seedKey: SeedKey) {
        (_uiStateFLow.value as? UiState.DataLoaded)?.data?.let {
            _uiStateFLow.value = it.copy(
                seedKeyState = SeedKeyState.Init,
                currentSeedGuesses = it.currentSeedGuesses.toMutableList().apply {
                    val index = indexOf(seedKey)
                    removeAt(index)
                    add(index, null)
                }
            ).toDataLoaded()
        }
    }

    fun verifySeedKeys() {
        (_uiStateFLow.value as? UiState.DataLoaded)?.data?.let {
            val invalidSeedKeys = it.seedKeys != it.currentSeedGuesses
            val seedKeyState =
                if (invalidSeedKeys) SeedKeyState.NotValid else SeedKeyState.Verifying

            val uiModel = it.copy(
                seedKeyState = seedKeyState,
                currentSeedGuesses = if (invalidSeedKeys) it.createEmptyGuesses() else it.currentSeedGuesses
            )

            _uiStateFLow.value = uiModel.toDataLoaded()

            if (seedKeyState == SeedKeyState.Verifying) createAccount(uiModel)
        }
    }

    private fun createAccount(uiModel: RecoveryPhraseUiModel) {
        viewModelScope.launch {
            delay(3000L)
            _uiStateFLow.value = uiModel.copy(seedKeyState = SeedKeyState.Finish).toDataLoaded()

//            /**
//             * TODO: Figure out account name. It cannot be null or blank
//             */
//            val accountName = "TODO"
//
//            /**
//             * TODO: Figure out what type this is
//             */
//            val addAccountPayload =
//                when {
//                    accountName.isNotBlank() -> AddAccountPayload.MetaAccount(accountName = accountName)
//                    else -> AddAccountPayload.ChainAccount(
//                        chainId = "TODO",
//                        metaId = 0L
//                    )
//                }
//
//            accountUseCase.createAccount(
//                mnemonicString = uiModel.mnemonicString,
//                addAccountPayload = addAccountPayload
//            ).onSuccess {
//                _uiStateFLow.value = uiModel.copy(seedKeyState = SeedKeyState.Finish).toDataLoaded()
//            }.onFailure {
//                /**
//                 * TODO: What should we show for an error?
//                 */
//            }
        }
    }
}