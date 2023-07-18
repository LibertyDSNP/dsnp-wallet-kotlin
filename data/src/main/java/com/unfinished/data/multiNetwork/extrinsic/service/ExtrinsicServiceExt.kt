package com.unfinished.data.multiNetwork.extrinsic.service

import com.unfinished.data.multiNetwork.chain.model.Chain
import com.unfinished.data.multiNetwork.extrinsic.ExtrinsicStatus
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
