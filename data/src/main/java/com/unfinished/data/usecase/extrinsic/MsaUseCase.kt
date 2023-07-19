package com.unfinished.data.usecase.extrinsic

import com.unfinished.data.R
import com.unfinished.data.model.event.EventType
import com.unfinished.data.model.account.MetaAccount
import com.unfinished.data.multiNetwork.chain.model.Chain
import com.unfinished.data.multiNetwork.extrinsic.calls.createMsa
import com.unfinished.data.multiNetwork.extrinsic.model.MsaResponse
import com.unfinished.data.multiNetwork.extrinsic.model.error.castToException
import com.unfinished.data.multiNetwork.extrinsic.service.ExtrinsicService
import com.unfinished.data.util.resource.ResourceManager

class MsaUseCase(
    private val realExtrinsicService: ExtrinsicService,
    private val resourceManager: ResourceManager
) {
    suspend fun createMsa(
        chain: Chain,
        metaAccount: MetaAccount,
        onReponse: (Result<MsaResponse>) -> Unit
    ) = realExtrinsicService.submitAndWatchExtrinsic(
        metaAccount = metaAccount,
        chain = chain,
        extrinsicCall = { createMsa() },
        encodedExtrinsic = {},
        failure = {
            onReponse(Result.failure(it.castToException(resourceManager.getString(R.string.create_msa_error))))
        },
    ) { _, events ->
        val msaEvent = events.map { it as? EventType.MsaEvent }.filterNotNull()
        onReponse(Result.success(MsaResponse(msaEvent.firstOrNull())))
    }

}