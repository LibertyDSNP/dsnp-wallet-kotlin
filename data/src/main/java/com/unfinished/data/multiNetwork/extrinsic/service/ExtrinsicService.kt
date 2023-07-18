package com.unfinished.data.multiNetwork.extrinsic.service

import com.unfinished.data.model.FeeResponse
import com.unfinished.data.model.MetaAccount
import com.unfinished.data.multiNetwork.chain.model.Chain
import com.unfinished.data.multiNetwork.chain.model.ChainId
import com.unfinished.data.multiNetwork.extrinsic.ExtrinsicStatus
import jp.co.soramitsu.fearless_utils.runtime.AccountId
import jp.co.soramitsu.fearless_utils.runtime.extrinsic.ExtrinsicBuilder
import kotlinx.coroutines.flow.Flow
import java.math.BigInteger

typealias FormExtrinsicWithOrigin = suspend ExtrinsicBuilder.(origin: AccountId) -> Unit

interface ExtrinsicService {

    suspend fun submitExtrinsicWithSelectedWallet(
        account: MetaAccount,
        chain: Chain,
        formExtrinsic: FormExtrinsicWithOrigin,
    ): Result<String>

    suspend fun submitAndWatchExtrinsicWithSelectedWallet(
        account: MetaAccount,
        chain: Chain,
        formExtrinsic: FormExtrinsicWithOrigin,
    ): Flow<ExtrinsicStatus>

    suspend fun submitExtrinsicWithAnySuitableWallet(
        chain: Chain,
        metaAccount: MetaAccount,
        formExtrinsic: FormExtrinsicWithOrigin,
    ): Result<String>

    suspend fun submitAndWatchExtrinsicAnySuitableWallet(
        chain: Chain,
        metaAccount: MetaAccount,
        formExtrinsic: FormExtrinsicWithOrigin,
    ): Flow<ExtrinsicStatus>

    suspend fun paymentInfo(
        chain: Chain,
        formExtrinsic: suspend ExtrinsicBuilder.() -> Unit,
    ): FeeResponse

    suspend fun estimateFee(
        chain: Chain,
        formExtrinsic: suspend ExtrinsicBuilder.() -> Unit,
    ): BigInteger

    suspend fun estimateFee(chainId: ChainId, extrinsic: String): BigInteger
}
