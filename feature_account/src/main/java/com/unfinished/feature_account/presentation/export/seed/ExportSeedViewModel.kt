package com.unfinished.feature_account.presentation.export.seed

import com.unfinished.feature_account.domain.account.export.seed.ExportSeedInteractor
import com.unfinished.feature_account.presentation.AccountRouter
import com.unfinished.feature_account.presentation.AdvancedEncryptionCommunicator
import com.unfinished.feature_account.presentation.AdvancedEncryptionRequester
import com.unfinished.feature_account.presentation.export.ExportPayload
import com.unfinished.feature_account.presentation.export.ExportViewModel
import com.unfinished.feature_account.presentation.model.account.AdvancedEncryptionPayload
import dagger.hilt.android.lifecycle.HiltViewModel
import io.novafoundation.nova.common.utils.flowOf
import io.novafoundation.nova.common.utils.inBackground
import javax.inject.Inject

@HiltViewModel
class ExportSeedViewModel @Inject constructor(
    private val router: AccountRouter,
    private val interactor: ExportSeedInteractor,
    private val advancedEncryptionCommunicator: AdvancedEncryptionCommunicator
) : ExportViewModel() {

    lateinit var exportPayload: ExportPayload

    val seedFlow by lazy {
        flowOf {
            interactor.getAccountSeed(exportPayload.metaId, exportPayload.chainId)
        }
            .inBackground()
            .share()
    }

    fun init(exportPayload: ExportPayload){
        this.exportPayload = exportPayload
    }

    fun optionsClicked() {
        val viewRequest = AdvancedEncryptionPayload.View(exportPayload.metaId, exportPayload.chainId)

        advancedEncryptionCommunicator.openRequest(viewRequest)
    }

    fun back() {
        router.back()
    }
}
