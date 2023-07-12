package com.unfinished.account.presentation.export.seed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.unfinished.account.domain.account.export.seed.ExportSeedInteractor
import com.unfinished.account.presentation.AccountRouter
import com.unfinished.account.presentation.AdvancedEncryptionCommunicator
import com.unfinished.account.presentation.export.ExportPayload
import com.unfinished.account.presentation.export.ExportViewModel
import com.unfinished.account.presentation.model.account.AdvancedEncryptionPayload
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import com.unfinished.common.utils.flowOf
import com.unfinished.common.utils.inBackground

class ExportSeedViewModel @AssistedInject constructor(
    private val router: AccountRouter,
    private val interactor: ExportSeedInteractor,
    private val advancedEncryptionCommunicator: AdvancedEncryptionCommunicator,
    @Assisted private val exportPayload: ExportPayload
) : ExportViewModel() {

    val seedFlow by lazy {
        flowOf {
            interactor.getAccountSeed(exportPayload.metaId, exportPayload.chainId)
        }
            .inBackground()
            .share()
    }

    fun optionsClicked() {
        val viewRequest = AdvancedEncryptionPayload.View(exportPayload.metaId, exportPayload.chainId)

        advancedEncryptionCommunicator.openRequest(viewRequest)
    }

    fun back() {
        router.back()
    }

    companion object {
        fun provideFactory(
            assistedFactory: AssistedFactory,
            payload: ExportPayload
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.injectPayload(payload) as T
            }
        }
    }

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun injectPayload(payload: ExportPayload): ExportSeedViewModel
    }
}
