package com.unfinished.feature_account.presentation.export.json.confirm

import com.unfinished.feature_account.presentation.AccountRouter
import com.unfinished.feature_account.presentation.AdvancedEncryptionCommunicator
import com.unfinished.feature_account.presentation.AdvancedEncryptionRequester
import com.unfinished.feature_account.presentation.export.ExportViewModel
import com.unfinished.feature_account.presentation.model.account.AdvancedEncryptionPayload
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExportJsonConfirmViewModel @Inject constructor(
    private val router: AccountRouter,
    private val advancedEncryptionCommunicator: AdvancedEncryptionCommunicator
) : ExportViewModel() {

    lateinit var payload: ExportJsonConfirmPayload

    val json by lazy { payload.json }

    fun init(payload: ExportJsonConfirmPayload){
        this.payload = payload
    }

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
}
