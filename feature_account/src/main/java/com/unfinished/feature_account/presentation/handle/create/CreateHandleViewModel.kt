package com.unfinished.feature_account.presentation.handle.create

import com.unfinished.feature_account.presentation.AccountRouter
import dagger.hilt.android.lifecycle.HiltViewModel

import com.unfinished.common.base.BaseViewModel
import com.unfinished.common.view.ButtonState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class CreateHandleViewModel @Inject constructor(
    private val router: AccountRouter,
) : BaseViewModel() {

    private val _handleValidation = MutableStateFlow(ButtonState.DISABLED)
    val handleValidation: Flow<ButtonState> = _handleValidation

    fun validateHandle(handle: String) {
        val isValid = handle.length in 4..16
        _handleValidation.value = when(isValid){
            true -> ButtonState.NORMAL
            false -> ButtonState.DISABLED
        }
    }

    fun openTabScreen(skip: Boolean, identitySuccess: Boolean){
        router.openTabScreen(skip,identitySuccess)
    }

    fun openConfirmHandleScreen(handle: String){
        router.openConfirmHandleScreen(handle)
    }

}
