package com.unfinished.data.multiNetwork.extrinsic.service

import com.unfinished.data.multiNetwork.extrinsic.model.FeeResponse
import com.unfinished.data.model.account.MetaAccount
import com.unfinished.data.multiNetwork.chain.model.Chain
import com.unfinished.data.multiNetwork.chain.model.ChainId
import com.unfinished.data.multiNetwork.extrinsic.ExtrinsicStatus
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.composite.DictEnum
import jp.co.soramitsu.fearless_utils.runtime.extrinsic.ExtrinsicBuilder
import jp.co.soramitsu.fearless_utils.runtime.extrinsic.signer.Signer
import kotlinx.coroutines.flow.Flow
import java.math.BigInteger
interface ExtrinsicService {

    suspend fun <T> submitAndWatchExtrinsic(
        metaAccount: MetaAccount,
        chain: Chain,
        extrinsicCall: suspend ExtrinsicBuilder.() -> Unit,
        encodedExtrinsic: EncodedExtrinsic,
        failure: ErrorBinder<T>,
        result: ResultBinder<T>,
    )
    suspend fun submitAndWatchExtrinsicAnySuitableWallet(
        chain: Chain,
        metaAccount: MetaAccount,
        encodedExtrinsic: EncodedExtrinsic,
        extrinsicCall: suspend ExtrinsicBuilder.() -> Unit,
    ): Flow<ExtrinsicStatus>

    suspend fun paymentInfo(
        chain: Chain,
        formExtrinsic: suspend ExtrinsicBuilder.() -> Unit,
    ): FeeResponse

    suspend fun estimateFee(
        chain: Chain,
        fromExtrinsic: suspend ExtrinsicBuilder.() -> Unit,
    ): BigInteger

    suspend fun estimateFee(chainId: ChainId, extrinsic: String): BigInteger
    suspend fun generateSignatureProof(
        payload: ByteArray,
        metaAccount: MetaAccount
    ): DictEnum.Entry<*>
}
