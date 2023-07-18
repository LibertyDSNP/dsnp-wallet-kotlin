package com.unfinished.data.multiNetwork.extrinsic.service

import com.unfinished.data.util.orZero
import com.unfinished.common.utils.takeWhileInclusive
import com.unfinished.data.model.FeeResponse
import com.unfinished.data.util.tip
import com.unfinished.runtime.signer.SignerProvider
import com.unfinished.data.multiNetwork.chain.model.Chain
import com.unfinished.data.multiNetwork.chain.model.ChainId
import com.unfinished.runtime.model.accountIdIn
import com.unfinished.data.multiNetwork.extrinsic.ExtrinsicBuilderFactory
import com.unfinished.data.multiNetwork.extrinsic.ExtrinsicStatus
import com.unfinished.data.multiNetwork.extrinsic.create
import com.unfinished.data.multiNetwork.rpc.RpcCalls
import com.unfinished.runtime.model.MetaAccount
import jp.co.soramitsu.fearless_utils.extensions.toHexString
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.fromHex
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.generics.Extrinsic
import jp.co.soramitsu.fearless_utils.runtime.extrinsic.ExtrinsicBuilder
import kotlinx.coroutines.flow.Flow
import java.math.BigInteger

class RealExtrinsicService(
    private val rpcCalls: RpcCalls,
    private val extrinsicBuilderFactory: ExtrinsicBuilderFactory,
    private val signerProvider: SignerProvider,
) : ExtrinsicService {

    override suspend fun submitExtrinsicWithSelectedWallet(
        metaAccount: MetaAccount,
        chain: Chain,
        formExtrinsic: FormExtrinsicWithOrigin,
    ): Result<String> {
        val accountId = metaAccount.accountIdIn(chain)!!
        return submitExtrinsicWithAnySuitableWallet(chain, metaAccount, formExtrinsic)
    }

    override suspend fun submitAndWatchExtrinsicWithSelectedWallet(
        metaAccount: MetaAccount,
        chain: Chain,
        formExtrinsic: FormExtrinsicWithOrigin,
    ): Flow<ExtrinsicStatus> {
        val accountId = metaAccount.accountIdIn(chain)!!

        return submitAndWatchExtrinsicAnySuitableWallet(chain, metaAccount, formExtrinsic)
    }

    override suspend fun submitExtrinsicWithAnySuitableWallet(
        chain: Chain,
        metaAccount: MetaAccount,
        formExtrinsic: FormExtrinsicWithOrigin,
    ): Result<String> = runCatching {
        val extrinsic = buildExtrinsic(chain, metaAccount, formExtrinsic)

        rpcCalls.submitExtrinsic(chain.id, extrinsic)
    }

    override suspend fun submitAndWatchExtrinsicAnySuitableWallet(
        chain: Chain,
        metaAccount: MetaAccount,
        formExtrinsic: FormExtrinsicWithOrigin,
    ): Flow<ExtrinsicStatus> {
        val extrinsic = buildExtrinsic(chain, metaAccount, formExtrinsic)

        return rpcCalls.submitAndWatchExtrinsic(chain.id, extrinsic)
            .takeWhileInclusive { !it.terminal }
    }

    override suspend fun paymentInfo(
        chain: Chain,
        formExtrinsic: suspend ExtrinsicBuilder.() -> Unit,
    ): FeeResponse {
        val extrinsic = extrinsicBuilderFactory.createForFee(chain)
            .also { it.formExtrinsic() }
            .build()

        return rpcCalls.getExtrinsicFee(chain.id, extrinsic)
    }

    override suspend fun estimateFee(
        chain: Chain,
        formExtrinsic: suspend ExtrinsicBuilder.() -> Unit,
    ): BigInteger {
        val extrinsicBuilder = extrinsicBuilderFactory.createForFee(chain)
        extrinsicBuilder.formExtrinsic()
        val extrinsic = extrinsicBuilder.build()

        val extrinsicType = Extrinsic.create(extrinsicBuilder.runtime)
        val decodedExtrinsic = extrinsicType.fromHex(extrinsicBuilder.runtime, extrinsic)

        val tip = decodedExtrinsic.tip().orZero()
        val baseFee = rpcCalls.getExtrinsicFee(chain.id, extrinsic).partialFee

        return tip + baseFee
    }

    override suspend fun estimateFee(chainId: ChainId, extrinsic: String): BigInteger {
        return rpcCalls.getExtrinsicFee(chainId, extrinsic).partialFee
    }

    private suspend fun buildExtrinsic(
        chain: Chain,
        metaAccount: MetaAccount,
        formExtrinsic: FormExtrinsicWithOrigin,
    ): String {
        val signer = signerProvider.signerFor(metaAccount)
        val accountId =  metaAccount.accountIdIn(chain)!!
        val extrinsicBuilder = extrinsicBuilderFactory.create(chain, signer, accountId)

        extrinsicBuilder.formExtrinsic(accountId)

        return extrinsicBuilder.build(useBatchAll = true)
    }
}
