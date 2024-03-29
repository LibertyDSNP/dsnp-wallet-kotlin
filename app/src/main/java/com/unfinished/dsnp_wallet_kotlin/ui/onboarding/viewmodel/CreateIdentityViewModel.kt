package com.unfinished.dsnp_wallet_kotlin.ui.onboarding.viewmodel

import androidx.lifecycle.viewModelScope
import com.unfinished.common.base.BaseViewModel
import com.unfinished.dsnp_wallet_kotlin.ui.onboarding.uimodel.CreateIdentityUiModel
import com.unfinished.dsnp_wallet_kotlin.ui.onboarding.uimodel.RestoreWalletUiModel
import com.unfinished.dsnp_wallet_kotlin.usecase.AccountUseCase
import com.unfinished.uikit.UiState
import com.unfinished.uikit.toDataLoaded
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CreateIdentityViewModel @Inject constructor(
    private val accountUseCase: AccountUseCase
) : BaseViewModel() {

    private companion object {
        const val MIN_HANDLE_LENGTH = 4
        const val MAX_HANDLE_LENGTH = 16
    }

    private val _uiStateFLow =
        MutableStateFlow<UiState<CreateIdentityUiModel>>(CreateIdentityUiModel().toDataLoaded())
    val uiStateFLow = _uiStateFLow.asStateFlow()

    private var tempFlagForLoading: Boolean = false

    private val handleRegex = "^[a-zA-Z0-9_]*\$".toRegex()

    /**
     * Returns true if we want to trigger the back button
     */
    fun previousStep(): Boolean = (_uiStateFLow.value as? UiState.DataLoaded)?.data?.let {
        return@let when {
            it.currentStep > 1 -> {
                _uiStateFLow.value = it.copy(currentStep = it.currentStep - 1).toDataLoaded()
                false
            }

            it.currentStep == 1 -> true
            else -> true
        }
    } ?: true


    fun nextStep() {
        (_uiStateFLow.value as? UiState.DataLoaded)?.data?.let {
            when {
                it.currentStep < it.totalSteps -> {
                    _uiStateFLow.value = it.copy(currentStep = it.currentStep + 1).toDataLoaded()
                }

                it.currentStep == it.totalSteps -> createHandle(it)
            }
        }
    }

    fun updateHandle(handle: String) {
        (_uiStateFLow.value as? UiState.DataLoaded)?.data?.let {
            when {
                handle.length > MAX_HANDLE_LENGTH -> return
                !handleRegex.matches(handle) -> return
            }

            _uiStateFLow.value = it.copy(
                handle = handle,
                handleIsValid = handle.isNotBlank() && handle.length >= MIN_HANDLE_LENGTH
            ).toDataLoaded()
        }
    }

    fun onRecoveryPhraseChange(text: String) {
        (_uiStateFLow.value as? UiState.DataLoaded)?.data?.let {
            _uiStateFLow.value = it.copy(
                restoreWalletUiModel = it.restoreWalletUiModel.copy(recoveryPhrase = text)
            ).toDataLoaded()
        }
    }

    fun showRecoveryPhrase() {
        (_uiStateFLow.value as? UiState.DataLoaded)?.data?.let {
            _uiStateFLow.value = it.copy(
                restoreWalletUiModel = it.restoreWalletUiModel.copy(
                    state = RestoreWalletUiModel.State.Init,
                    recoveryPhrase = ""
                )
            ).toDataLoaded()
        }
    }

    fun showRecoveryPhraseError() {
        (_uiStateFLow.value as? UiState.DataLoaded)?.data?.let {
            _uiStateFLow.value = it.copy(
                restoreWalletUiModel = it.restoreWalletUiModel.copy(state = RestoreWalletUiModel.State.Error)
            ).toDataLoaded()
        }
    }

    fun showRecoveryPhraseLoading() {
        (_uiStateFLow.value as? UiState.DataLoaded)?.data?.let {
            _uiStateFLow.value = it.copy(
                restoreWalletUiModel = it.restoreWalletUiModel.copy(state = RestoreWalletUiModel.State.Loading)
            ).toDataLoaded()


            /**
             * This is temp code to show different phases, this can be removed once connected
             * to service
             */
            viewModelScope.launch {
                delay(3000L)

                if (!tempFlagForLoading) {
                    showRecoveryPhraseError()
                    tempFlagForLoading = true
                } else {
                    _uiStateFLow.value = GoToIdentityFromImport
                }
            }
        }
    }

    private fun createHandle(uiModel: CreateIdentityUiModel) {
        _uiStateFLow.value = uiModel.copy(showLoading = true).toDataLoaded()

        viewModelScope.launch {
            runCatching {
                accountUseCase.createHandle(uiModel.handle)
                accountUseCase.fetchHandle()
            }.onSuccess {
                _uiStateFLow.value = GoToIdentityFromCreate(username = it)
            }.onFailure {
                Timber.e(it)
            }
        }
    }

    class GoToIdentityFromCreate(val username: String) : UiState<CreateIdentityUiModel>
    object GoToIdentityFromImport : UiState<CreateIdentityUiModel>
}