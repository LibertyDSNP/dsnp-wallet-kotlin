package com.unfinished.dsnp_wallet_kotlin.ui.common.dialog.viewmodel

import com.unfinished.dsnp_wallet_kotlin.ui.NavGraph
import com.unfinished.dsnp_wallet_kotlin.ui.destinations.DirectionDestination
import com.unfinished.uikit.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.novafoundation.nova.common.base.BaseViewModel
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
        letsGoDirection: DirectionDestination
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
            val letsGoDirection: DirectionDestination
        ) : State()
    }
}