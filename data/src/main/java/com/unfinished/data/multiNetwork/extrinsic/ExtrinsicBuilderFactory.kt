package com.unfinished.data.multiNetwork.extrinsic

import com.unfinished.data.util.ext.orZero
import com.unfinished.data.util.ext.addressOf
import com.unfinished.data.multiNetwork.ChainRegistry
import com.unfinished.data.multiNetwork.chain.model.Chain
import com.unfinished.data.multiNetwork.getRuntime
import com.unfinished.data.multiNetwork.rpc.RpcCalls
import com.unfinished.data.util.ext.genesisHash
import jp.co.soramitsu.fearless_utils.extensions.fromHex
import jp.co.soramitsu.fearless_utils.runtime.AccountId
import jp.co.soramitsu.fearless_utils.runtime.extrinsic.ExtrinsicBuilder
import jp.co.soramitsu.fearless_utils.runtime.extrinsic.signer.Signer
import java.math.BigInteger

class ExtrinsicBuilderFactory(
    private val rpcCalls: RpcCalls,
    private val chainRegistry: ChainRegistry,
    private val mortalityConstructor: MortalityConstructor,
) {

    /**
     * Create with special signer for fee calculation
     */
    suspend fun createForFee(
        chain: Chain,
    ): ExtrinsicBuilder {
        val signer = FeeSigner(chain)

        return create(chain, signer, signer.accountId())
    }

    /**
     * Create with real keypair
     */
    suspend fun create(
        chain: Chain,
        signer: Signer,
        accountId: AccountId,
    ): ExtrinsicBuilder {
        val runtime = chainRegistry.getRuntime(chain.id)

        val accountAddress = chain.addressOf(accountId)

        val nonce = rpcCalls.getNonce(chain.id, accountAddress)
        val runtimeVersion = rpcCalls.getRuntimeVersion(chain.id)
        val mortality = mortalityConstructor.constructMortality(chain.id)
        val gensisHash = rpcCalls.getBlockHash(chain.id, BigInteger.ZERO)

        return ExtrinsicBuilder(
            tip = chain.additional?.defaultTip.orZero(),
            runtime = runtime,
            nonce = nonce,
            runtimeVersion = runtimeVersion,
            genesisHash = gensisHash.fromHex(),
            blockHash = mortality.blockHash.fromHex(),
            era = mortality.era,
            customSignedExtensions = CustomSignedExtensions.extensionsWithValues(runtime),
            signer = signer,
            accountId = accountId
        )
    }
}
