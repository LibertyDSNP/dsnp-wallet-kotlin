package com.unfinished.data.usecase.extrinsic

import com.unfinished.data.R
import com.unfinished.data.model.account.MetaAccount
import com.unfinished.data.multiNetwork.ChainRegistry
import com.unfinished.data.multiNetwork.chain.model.Chain
import com.unfinished.data.multiNetwork.extrinsic.calls.createProvider
import com.unfinished.data.multiNetwork.extrinsic.calls.transferCall
import com.unfinished.data.multiNetwork.extrinsic.model.error.castToException
import com.unfinished.data.multiNetwork.extrinsic.service.ExtrinsicService
import com.unfinished.data.repository.event.EventsRepository
import com.unfinished.data.util.ext.accountIdOf
import com.unfinished.data.util.resource.ResourceManager
import com.unfinished.data.util.toPlanks
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class BalanceUseCase @Inject constructor(
    private val realExtrinsicService: ExtrinsicService,
    private val resourceManager: ResourceManager
) {
    suspend fun transfer(
        chain: Chain,
        metaAccount: MetaAccount,
        destMetaAccount: MetaAccount,
        amount: Float,
    ) = callbackFlow<Result<Nothing>> {
        realExtrinsicService.submitAndWatchExtrinsic(
            metaAccount = metaAccount,
            chain = chain,
            extrinsicCall = {
                transferCall(
                    destAccount = destMetaAccount.substrateAccountId!!,
                    amount = amount.toPlanks()
                )
            },
            encodedExtrinsic = {},
            failure = {
                trySend(Result.failure(it.castToException(resourceManager.getString(R.string.transfer_error))))
            },
            result = { _, _ -> }
        )
        awaitClose()
    }

}