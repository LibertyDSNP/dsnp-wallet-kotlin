package io.novafoundation.nova.feature_account_impl.presentation.exporting.seed

import com.unfinished.feature_account.domain.account.export.seed.ExportSeedInteractor
import com.unfinished.feature_account.presentation.AccountRouter
import com.unfinished.feature_account.presentation.AdvancedEncryptionRequester
import com.unfinished.feature_account.presentation.export.ExportPayload
import com.unfinished.feature_account.presentation.export.ExportViewModel
import com.unfinished.feature_account.presentation.model.account.AdvancedEncryptionPayload
import io.novafoundation.nova.common.utils.flowOf
import io.novafoundation.nova.common.utils.inBackground

class ExportSeedViewModel(
    private val router: AccountRouter,
    private val interactor: ExportSeedInteractor,
    private val advancedEncryptionRequester: AdvancedEncryptionRequester,
    private val exportPayload: ExportPayload,
) : ExportViewModel() {

    val seedFlow = flowOf {
        interactor.getAccountSeed(exportPayload.metaId, exportPayload.chainId)
    }
        .inBackground()
        .share()

    fun optionsClicked() {
        val viewRequest = AdvancedEncryptionPayload.View(exportPayload.metaId, exportPayload.chainId)

        advancedEncryptionRequester.openRequest(viewRequest)
    }

    fun back() {
        router.back()
    }
}
