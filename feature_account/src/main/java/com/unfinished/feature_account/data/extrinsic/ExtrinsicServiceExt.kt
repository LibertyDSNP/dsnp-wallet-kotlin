package com.unfinished.feature_account.data.extrinsic

import com.unfinished.runtime.extrinsic.ExtrinsicStatus
import com.unfinished.runtime.multiNetwork.chain.model.Chain
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first

suspend fun ExtrinsicService.submitExtrinsicWithSelectedWalletAndWaitBlockInclusion(
    chain: Chain,
    formExtrinsic: FormExtrinsicWithOrigin,
): Result<ExtrinsicStatus.InBlock> = runCatching {
    submitAndWatchExtrinsicWithSelectedWallet(chain, formExtrinsic)
        .filterIsInstance<ExtrinsicStatus.InBlock>()
        .first()
}
