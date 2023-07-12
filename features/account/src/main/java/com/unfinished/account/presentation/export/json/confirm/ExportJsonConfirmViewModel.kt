package com.unfinished.account.presentation.export.json.confirm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.unfinished.account.presentation.AccountRouter
import com.unfinished.account.presentation.AdvancedEncryptionCommunicator
import com.unfinished.account.presentation.export.ExportViewModel
import com.unfinished.account.presentation.model.account.AdvancedEncryptionPayload
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class ExportJsonConfirmViewModel @AssistedInject constructor(
    private val router: AccountRouter,
    private val advancedEncryptionCommunicator: AdvancedEncryptionCommunicator,
    @Assisted private val payload: ExportJsonConfirmPayload
) : ExportViewModel() {

    val json by lazy { payload.json }

    fun back() {
        router.back()
    }

    fun confirmClicked() {
        exportText(json)
    }

    fun optionsClicked() {
        val viewRequest = AdvancedEncryptionPayload.View(payload.exportPayload.metaId, payload.exportPayload.chainId, hideDerivationPaths = true)

        advancedEncryptionCommunicator.openRequest(viewRequest)
    }

    companion object {
        fun provideFactory(
            assistedFactory: AssistedFactory,
            payload: ExportJsonConfirmPayload
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.injectPayload(payload) as T
            }
        }
    }

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun injectPayload(payload: ExportJsonConfirmPayload): ExportJsonConfirmViewModel
    }
}
