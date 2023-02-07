package com.unfinished.feature_account.presentation.export.json.confirm

import com.unfinished.feature_account.presentation.AccountRouter
import com.unfinished.feature_account.presentation.AdvancedEncryptionRequester
import com.unfinished.feature_account.presentation.export.ExportViewModel
import com.unfinished.feature_account.presentation.model.account.AdvancedEncryptionPayload

class ExportJsonConfirmViewModel(
    private val router: AccountRouter,
    private val advancedEncryptionRequester: AdvancedEncryptionRequester,
    private val payload: ExportJsonConfirmPayload
) : ExportViewModel() {

    val json = payload.json

    fun back() {
        router.back()
    }

    fun confirmClicked() {
        exportText(json)
    }

    fun optionsClicked() {
        val viewRequest = AdvancedEncryptionPayload.View(payload.exportPayload.metaId, payload.exportPayload.chainId, hideDerivationPaths = true)

        advancedEncryptionRequester.openRequest(viewRequest)
    }
}
