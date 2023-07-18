package com.unfinished.data.multiNetwork.extrinsic

import com.unfinished.data.util.orZero
import com.unfinished.data.util.addressOf
import com.unfinished.data.multiNetwork.ChainRegistry
import com.unfinished.data.multiNetwork.chain.model.Chain
import com.unfinished.data.multiNetwork.getRuntime
import com.unfinished.data.multiNetwork.rpc.RpcCalls
import jp.co.soramitsu.fearless_utils.extensions.fromHex
import jp.co.soramitsu.fearless_utils.runtime.AccountId
import jp.co.soramitsu.fearless_utils.runtime.extrinsic.ExtrinsicBuilder
import jp.co.soramitsu.fearless_utils.runtime.extrinsic.signer.Signer

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

        return ExtrinsicBuilder(
            tip = chain.additional?.defaultTip.orZero(),
            runtime = runtime,
            nonce = nonce,
            runtimeVersion = runtimeVersion,
            genesisHash = (chain.ghash ?: "0x5b9ae6fd47d88a084767da3d544d3334e5804c1201005c1bd6dd59f23ed57350").fromHex(),
            blockHash = mortality.blockHash.fromHex(),
            era = mortality.era,
            customSignedExtensions = CustomSignedExtensions.extensionsWithValues(runtime),
            signer = signer,
            accountId = accountId
        )
    }
}
