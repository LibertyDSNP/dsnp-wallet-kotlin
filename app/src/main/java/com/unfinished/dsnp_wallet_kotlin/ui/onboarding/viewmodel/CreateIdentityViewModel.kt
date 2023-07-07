package com.unfinished.dsnp_wallet_kotlin.ui.onboarding.viewmodel

import androidx.lifecycle.viewModelScope
import com.unfinished.dsnp_wallet_kotlin.ui.onboarding.uimodel.CreateIdentityUiModel
import com.unfinished.dsnp_wallet_kotlin.ui.onboarding.uimodel.RestoreWalletUiModel
import com.unfinished.uikit.UiState
import com.unfinished.uikit.toDataLoaded
import dagger.hilt.android.lifecycle.HiltViewModel
import io.novafoundation.nova.common.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateIdentityViewModel @Inject constructor(

) : BaseViewModel() {
    private val _uiStateFLow =
        MutableStateFlow<UiState<CreateIdentityUiModel>>(CreateIdentityUiModel().toDataLoaded())
    val uiStateFLow = _uiStateFLow.asStateFlow()

    private var tempFlagForLoading: Boolean = false

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

                it.currentStep == it.totalSteps -> {
                    _uiStateFLow.value = GoToIdentityFromCreate(username = it.handle)
                }
            }
        }
    }

    fun updateHandle(handle: String) {
        (_uiStateFLow.value as? UiState.DataLoaded)?.data?.let {
            _uiStateFLow.value = it.copy(
                handle = handle,
                handleIsValid = handle.isNotBlank()
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
                restoreWalletUiModel = it.restoreWalletUiModel.copy(state = RestoreWalletUiModel.State.Init)
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

    class GoToIdentityFromCreate(val username: String) : UiState<CreateIdentityUiModel>
    object GoToIdentityFromImport : UiState<CreateIdentityUiModel>

}