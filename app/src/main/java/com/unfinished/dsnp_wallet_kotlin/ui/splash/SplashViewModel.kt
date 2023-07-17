package com.unfinished.dsnp_wallet_kotlin.ui.splash

import androidx.lifecycle.viewModelScope
import com.unfinished.account.domain.interfaces.AccountRepository
import com.unfinished.uikit.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import com.unfinished.common.base.BaseViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: AccountRepository
) : BaseViewModel() {

    private val _uiStateFLow = MutableStateFlow<UiState<Unit>>(UiState.Loading())
    val uiStateFLow = _uiStateFLow.asStateFlow()


    init {
        openInitialDestination()
    }

    private fun openInitialDestination() {
        viewModelScope.launch {
            val delay = async { delay(1000) }
            val updatedState = fetchUiState()
            awaitAll(delay, updatedState)
            _uiStateFLow.value = updatedState.await()
        }
    }

    private fun fetchUiState(): Deferred<UiState<Unit>> = async {
        if (repository.isAccountSelected()) {
            if (repository.isCodeSet()) {
                HeadToCheckPincode
            } else {
                HeadToCreatePincode
            }
        } else {
            HeadToLanding
        }
    }

    object HeadToLanding : UiState<Unit>
    object HeadToCreatePincode : UiState<Unit>
    object HeadToCheckPincode : UiState<Unit>

}
