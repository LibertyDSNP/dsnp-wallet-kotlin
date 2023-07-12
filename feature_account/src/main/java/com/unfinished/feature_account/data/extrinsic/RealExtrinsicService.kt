package com.unfinished.feature_account.data.extrinsic

import com.unfinished.data.util.orZero
import com.unfinished.common.utils.takeWhileInclusive
import com.unfinished.runtime.util.tip
import com.unfinished.feature_account.data.signer.SignerProvider
import com.unfinished.feature_account.domain.interfaces.AccountRepository
import com.unfinished.feature_account.domain.model.accountIdIn
import com.unfinished.runtime.extrinsic.ExtrinsicBuilderFactory
import com.unfinished.runtime.extrinsic.ExtrinsicStatus
import com.unfinished.runtime.extrinsic.create
import com.unfinished.runtime.multiNetwork.chain.model.Chain
import com.unfinished.runtime.multiNetwork.chain.model.ChainId
import com.unfinished.runtime.network.rpc.RpcCalls
import com.unfinished.runtime.network.runtime.model.FeeResponse
import jp.co.soramitsu.fearless_utils.extensions.toHexString
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.fromHex
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.generics.Extrinsic
import jp.co.soramitsu.fearless_utils.runtime.extrinsic.ExtrinsicBuilder
import kotlinx.coroutines.flow.Flow
import java.math.BigInteger

class RealExtrinsicService(
    private val rpcCalls: RpcCalls,
    private val accountRepository: AccountRepository,
    private val extrinsicBuilderFactory: ExtrinsicBuilderFactory,
    private val signerProvider: SignerProvider,
) : ExtrinsicService {

    override suspend fun submitExtrinsicWithSelectedWallet(
        chain: Chain,
        formExtrinsic: FormExtrinsicWithOrigin,
    ): Result<String> {
        val account = accountRepository.getSelectedMetaAccount()
        val accountId = account.accountIdIn(chain)!!

        return submitExtrinsicWithAnySuitableWallet(chain, accountId, formExtrinsic)
    }

    override suspend fun submitAndWatchExtrinsicWithSelectedWallet(
        chain: Chain,
        formExtrinsic: FormExtrinsicWithOrigin,
    ): Flow<ExtrinsicStatus> {
        val account = accountRepository.getSelectedMetaAccount()
        val accountId = account.accountIdIn(chain)!!

        return submitAndWatchExtrinsicAnySuitableWallet(chain, accountId, formExtrinsic)
    }

    override suspend fun submitExtrinsicWithAnySuitableWallet(
        chain: Chain,
        accountId: ByteArray,
        formExtrinsic: FormExtrinsicWithOrigin,
    ): Result<String> = runCatching {
        val extrinsic = buildExtrinsic(chain, accountId, formExtrinsic)

        rpcCalls.submitExtrinsic(chain.id, extrinsic)
    }

    override suspend fun submitAndWatchExtrinsicAnySuitableWallet(
        chain: Chain,
        accountId: ByteArray,
        formExtrinsic: FormExtrinsicWithOrigin,
    ): Flow<ExtrinsicStatus> {
        val extrinsic = buildExtrinsic(chain, accountId, formExtrinsic)

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
        accountId: ByteArray,
        formExtrinsic: FormExtrinsicWithOrigin,
    ): String {
        val metaAccount = accountRepository.findMetaAccount(accountId) ?: error("No meta account found accessing ${accountId.toHexString()}")
        val signer = signerProvider.signerFor(metaAccount)

        val extrinsicBuilder = extrinsicBuilderFactory.create(chain, signer, accountId)

        extrinsicBuilder.formExtrinsic(accountId)

        return extrinsicBuilder.build(useBatchAll = true)
    }
}
