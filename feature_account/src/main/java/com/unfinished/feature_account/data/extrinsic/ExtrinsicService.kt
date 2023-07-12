package com.unfinished.feature_account.data.extrinsic

import com.unfinished.runtime.extrinsic.ExtrinsicStatus
import com.unfinished.runtime.multiNetwork.chain.model.Chain
import com.unfinished.runtime.multiNetwork.chain.model.ChainId
import com.unfinished.runtime.network.runtime.model.FeeResponse
import jp.co.soramitsu.fearless_utils.runtime.AccountId
import jp.co.soramitsu.fearless_utils.runtime.extrinsic.ExtrinsicBuilder
import kotlinx.coroutines.flow.Flow
import java.math.BigInteger

typealias FormExtrinsicWithOrigin = suspend ExtrinsicBuilder.(origin: AccountId) -> Unit

interface ExtrinsicService {

    suspend fun submitExtrinsicWithSelectedWallet(
        chain: Chain,
        formExtrinsic: FormExtrinsicWithOrigin,
    ): Result<String>

    suspend fun submitAndWatchExtrinsicWithSelectedWallet(
        chain: Chain,
        formExtrinsic: FormExtrinsicWithOrigin,
    ): Flow<ExtrinsicStatus>

    suspend fun submitExtrinsicWithAnySuitableWallet(
        chain: Chain,
        accountId: ByteArray,
        formExtrinsic: FormExtrinsicWithOrigin,
    ): Result<String>

    suspend fun submitAndWatchExtrinsicAnySuitableWallet(
        chain: Chain,
        accountId: ByteArray,
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
