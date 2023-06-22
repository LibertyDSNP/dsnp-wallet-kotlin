package com.unfinished.dsnp_wallet_kotlin.ui.onboarding.viewmodel

import com.unfinished.dsnp_wallet_kotlin.ui.onboarding.uimodel.CreateIdentityUiModel
import com.unfinished.uikit.UiState
import com.unfinished.uikit.toDataLoaded
import dagger.hilt.android.lifecycle.HiltViewModel
import io.novafoundation.nova.common.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CreateIdentityViewModel @Inject constructor(

) : BaseViewModel() {
    private val _uiStateFLow =
        MutableStateFlow<UiState<CreateIdentityUiModel>>(CreateIdentityUiModel().toDataLoaded())
    val uiStateFLow = _uiStateFLow.asStateFlow()

    private val _visibleStateFlow =
        MutableStateFlow<UiState<Unit>>(HideCreateIdentity)
    val visibleStateFlow = _visibleStateFlow.asStateFlow()

    fun previousStep() {
        (_uiStateFLow.value as? UiState.DataLoaded)?.data?.let {
            when {
                it.currentStep > 1 -> {
                    _uiStateFLow.value = it.copy(currentStep = it.currentStep - 1).toDataLoaded()
                }

                it.currentStep == 1 -> hideCreateIdentity()
            }
        }
    }

    fun nextStep() {
        (_uiStateFLow.value as? UiState.DataLoaded)?.data?.let {
            when {
                it.currentStep < it.totalSteps -> {
                    _uiStateFLow.value = it.copy(currentStep = it.currentStep + 1).toDataLoaded()
                }

                it.currentStep == it.totalSteps -> {
                    _uiStateFLow.value = GoToIdentity
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

    fun showCreateIdentity() {
        _visibleStateFlow.value = ShowCreateIdentity
    }

    fun hideCreateIdentity() {
        _visibleStateFlow.value = HideCreateIdentity
    }

    object ShowCreateIdentity : UiState<Unit>
    object HideCreateIdentity : UiState<Unit>
    object GoToIdentity : UiState<CreateIdentityUiModel>

}