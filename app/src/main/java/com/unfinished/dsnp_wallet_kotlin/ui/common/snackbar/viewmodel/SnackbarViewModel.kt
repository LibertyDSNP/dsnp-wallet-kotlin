package com.unfinished.dsnp_wallet_kotlin.ui.common.snackbar.viewmodel

import com.unfinished.common.base.BaseViewModel
import com.unfinished.uikit.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SnackbarViewModel @Inject constructor(

) : BaseViewModel() {
    private val _stateFlow = MutableStateFlow<State>(State.Hide)
    val stateFlow = _stateFlow.asStateFlow()

    fun hide() {
        _stateFlow.value = State.Hide
    }

    fun showSuccess(text: String) {
        _stateFlow.value = State.ShowSuccess(text = text)
    }

    sealed class State(val text: String) : UiState<Unit> {
        object Hide : State("")
        class ShowSuccess(text: String) : State(text)
    }
}