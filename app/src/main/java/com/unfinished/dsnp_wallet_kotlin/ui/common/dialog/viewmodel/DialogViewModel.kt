package com.unfinished.dsnp_wallet_kotlin.ui.common.dialog.viewmodel

import com.ramcosta.composedestinations.spec.Direction
import com.unfinished.common.base.BaseViewModel
import com.unfinished.uikit.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DialogViewModel @Inject constructor(

) : BaseViewModel() {
    private val _stateFlow = MutableStateFlow<State>(State.Hide)
    val stateFlow = _stateFlow.asStateFlow()

    fun hide() {
        _stateFlow.value = State.Hide
    }

    fun showCongratulation(
        userName: String,
        letsGoDirection: Direction
    ) {
        _stateFlow.value = State.Congratulation(
            userName = userName,
            letsGoDirection = letsGoDirection
        )
    }

    sealed class State : UiState<Unit> {
        object Hide : State()
        class Congratulation(
            val userName: String,
            val letsGoDirection: Direction
        ) : State()
    }
}