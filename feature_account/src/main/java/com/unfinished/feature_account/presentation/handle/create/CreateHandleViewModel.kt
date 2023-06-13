package com.unfinished.feature_account.presentation.handle.create

import com.unfinished.feature_account.presentation.AccountRouter
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

import io.novafoundation.nova.common.base.BaseViewModel
import io.novafoundation.nova.common.validation.ValidationExecutor
import io.novafoundation.nova.common.view.ButtonState
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

    fun openConfirmHandleScreen(handle: String){
        router.openConfirmHandleScreen(handle)
    }

}
