package com.unfinished.dsnp_wallet_kotlin.ui.bottomsheet.viewmodel

import com.unfinished.dsnp_wallet_kotlin.ui.bottomsheet.uimodel.BottomSheetUiModel
import com.unfinished.uikit.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.novafoundation.nova.common.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class BottomSheetViewModel @Inject constructor(

) : BaseViewModel() {

    private val _stateFlow = MutableStateFlow<State>(State.Hide)
    val stateFlow = _stateFlow.asStateFlow()

    private val _uiModel = MutableStateFlow(BottomSheetUiModel())
    val uiModel = _uiModel.asStateFlow()

    fun hide() {
        _stateFlow.value = State.Hide
    }

    fun showCreateAccount() {
        _stateFlow.value = State.CreateAccount
    }

    fun showAgreeToUse() {
        _stateFlow.value = State.AgreeToUse
    }

    fun agreedToUse() {
        /**
         * TODO: Save this to shared pref
         */
        _uiModel.value = _uiModel.value.copy(
            showAgreeToTerms = false
        )
    }

    sealed class State : UiState<Unit> {
        object Hide : State()
        object CreateAccount : State()
        object AgreeToUse : State()
    }
}