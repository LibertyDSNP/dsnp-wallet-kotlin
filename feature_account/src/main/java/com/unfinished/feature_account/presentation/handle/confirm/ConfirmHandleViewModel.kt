package com.unfinished.feature_account.presentation.handle.confirm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.unfinished.feature_account.presentation.AccountRouter
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.novafoundation.nova.common.base.BaseViewModel

class ConfirmHandleViewModel @AssistedInject constructor(
    private val router: AccountRouter,
    @Assisted private val payload: ConfirmHandlePayload
) : BaseViewModel() {

    val handle by lazy { payload.handle }

    fun openTermsHandleScreen() {
        router.openTermsHandleScreen()
    }
    companion object {
        fun provideFactory(
            assistedFactory: AssistedFactory,
            payload: ConfirmHandlePayload
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.injectPayload(payload) as T
            }
        }
    }

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun injectPayload(payload: ConfirmHandlePayload): ConfirmHandleViewModel
    }
}
