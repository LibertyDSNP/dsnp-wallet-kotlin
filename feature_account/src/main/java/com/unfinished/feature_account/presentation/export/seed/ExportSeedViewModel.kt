package com.unfinished.feature_account.presentation.export.seed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.unfinished.feature_account.domain.account.export.seed.ExportSeedInteractor
import com.unfinished.feature_account.presentation.AccountRouter
import com.unfinished.feature_account.presentation.AdvancedEncryptionCommunicator
import com.unfinished.feature_account.presentation.AdvancedEncryptionRequester
import com.unfinished.feature_account.presentation.export.ExportPayload
import com.unfinished.feature_account.presentation.export.ExportViewModel
import com.unfinished.feature_account.presentation.mnemonic.backup.BackupMnemonicPayload
import com.unfinished.feature_account.presentation.mnemonic.backup.BackupMnemonicViewModel
import com.unfinished.feature_account.presentation.model.account.AdvancedEncryptionPayload
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.novafoundation.nova.common.utils.flowOf
import io.novafoundation.nova.common.utils.inBackground
import javax.inject.Inject

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
