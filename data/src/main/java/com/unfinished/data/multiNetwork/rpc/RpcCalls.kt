package com.unfinished.data.multiNetwork.rpc

import com.unfinished.data.multiNetwork.runtime.binding.BlockNumber
import com.unfinished.data.multiNetwork.runtime.binding.bindNumber
import com.unfinished.data.multiNetwork.runtime.binding.castToStruct
import com.unfinished.data.multiNetwork.runtime.binding.fromHexOrIncompatible
import com.unfinished.data.multiNetwork.extrinsic.model.FeeResponse
import com.unfinished.data.model.block.SignedBlock
import com.unfinished.data.model.block.SignedBlock.Block.Header
import com.unfinished.data.util.ext.extrinsicHash
import com.unfinished.data.util.ext.removeHexPrefix
import com.unfinished.data.multiNetwork.extrinsic.ExtrinsicStatus
import com.unfinished.data.multiNetwork.extrinsic.asExtrinsicStatus
import com.unfinished.data.multiNetwork.ChainRegistry
import com.unfinished.data.multiNetwork.chain.model.ChainId
import com.unfinished.data.multiNetwork.getRuntime
import com.unfinished.data.multiNetwork.rpc.calls.FeeCalculationRequest
import com.unfinished.data.multiNetwork.rpc.calls.GetBlockHashRequest
import com.unfinished.data.multiNetwork.rpc.calls.GetBlockRequest
import com.unfinished.data.multiNetwork.rpc.calls.GetFinalizedHeadRequest
import com.unfinished.data.multiNetwork.rpc.calls.GetHeaderRequest
import com.unfinished.data.multiNetwork.rpc.calls.GetStateRequest
import com.unfinished.data.multiNetwork.rpc.calls.NextAccountIndexRequest
import jp.co.soramitsu.fearless_utils.extensions.fromHex
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.primitives.u32
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.toHex
import jp.co.soramitsu.fearless_utils.wsrpc.executeAsync
import jp.co.soramitsu.fearless_utils.wsrpc.mappers.nonNull
import jp.co.soramitsu.fearless_utils.wsrpc.mappers.pojo
import jp.co.soramitsu.fearless_utils.wsrpc.request.DeliveryType
import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.author.SubmitAndWatchExtrinsicRequest
import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.author.SubmitExtrinsicRequest
import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.chain.RuntimeVersion
import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.chain.RuntimeVersionRequest
import jp.co.soramitsu.fearless_utils.wsrpc.response.RpcResponse
import jp.co.soramitsu.fearless_utils.wsrpc.subscriptionFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.math.BigInteger

private const val FEE_DECODE_TYPE = "RuntimeDispatchInfo"

@Suppress("EXPERIMENTAL_API_USAGE")
class RpcCalls(
    private val chainRegistry: ChainRegistry,
) {

    suspend fun getExtrinsicFee(chainId: ChainId, extrinsic: String): FeeResponse {
        val runtime = chainRegistry.getRuntime(chainId)
        val type = runtime.typeRegistry[FEE_DECODE_TYPE]

        return if (type != null) {
            val lengthInBytes = extrinsic.fromHex().size
            val encodedLength = u32.toHex(runtime, lengthInBytes.toBigInteger()).removeHexPrefix()
            val param = extrinsic + encodedLength
            val request = StateCallRequest("TransactionPaymentApi_query_info", param)
            val response = socketFor(chainId).executeAsync(request, mapper = pojo<String>().nonNull())
            val decoded = type.fromHexOrIncompatible(response, runtime)

            bindPartialFee(decoded)
        } else {
            val request = FeeCalculationRequest(extrinsic)
            socketFor(chainId).executeAsync(request, mapper = pojo<FeeResponse>().nonNull())
        }
    }

    suspend fun submitExtrinsic(chainId: ChainId, extrinsic: String): String {

        val request = SubmitExtrinsicRequest(extrinsic)

        return socketFor(chainId).executeAsync(
            request,
            mapper = pojo<String>().nonNull(),
            deliveryType = DeliveryType.AT_MOST_ONCE
        )
    }

    fun submitAndWatchExtrinsic(chainId: ChainId, extrinsic: String): Flow<ExtrinsicStatus> {
        return flow {
            val hash = extrinsic.extrinsicHash()
            val request = SubmitAndWatchExtrinsicRequest(extrinsic)
            val inner = socketFor(chainId).subscriptionFlow(request, unsubscribeMethod = "author_unwatchExtrinsic")
                .map { it.asExtrinsicStatus(hash) }
            emitAll(inner)
        }
    }

     suspend fun getNonce(chainId: ChainId, accountAddress: String): BigInteger {
        val nonceRequest = NextAccountIndexRequest(accountAddress)

        val response = socketFor(chainId).executeAsync(nonceRequest)
        val doubleResult = response.result as Double

        return doubleResult.toInt().toBigInteger()
    }

    suspend fun getRuntimeVersion(chainId: ChainId): RuntimeVersion {
        val request = RuntimeVersionRequest()

        return socketFor(chainId).executeAsync(request, mapper = pojo<RuntimeVersion>().nonNull())
    }

    /**
     * Retrieves the block with given hash
     * If hash is null, than the latest block is returned
     */
    suspend fun getBlock(chainId: ChainId, hash: String? = null): SignedBlock {
        val blockRequest = GetBlockRequest(hash)

        return socketFor(chainId).executeAsync(blockRequest, mapper = pojo<SignedBlock>().nonNull())
    }

    /**
     * Retrieves account details
     */
    suspend fun getStateAccount(chainId: String, key: String): RpcResponse {
        val stateRequest = GetStateRequest(key)
        return socketFor(chainId).executeAsync(stateRequest)
    }

    /**
     * Get hash of the last finalized block in the canon chain
     */
    suspend fun getFinalizedHead(chainId: ChainId): String {
        return socketFor(chainId).executeAsync(GetFinalizedHeadRequest, mapper = pojo<String>().nonNull())
    }

    /**
     * Retrieves the header for a specific block
     *
     * @param hash - hash of the block. If null - then the  best pending header is returned
     */
    suspend fun getBlockHeader(chainId: ChainId, hash: String? = null): Header {
        return socketFor(chainId).executeAsync(GetHeaderRequest(hash), mapper = pojo<Header>().nonNull())
    }

    /**
     * Retrieves the hash of a specific block
     *
     *  @param blockNumber - if null, then the  best block hash is returned
     */
    suspend fun getBlockHash(chainId: ChainId, blockNumber: BlockNumber? = null): String {
        return socketFor(chainId).executeAsync(GetBlockHashRequest(blockNumber), mapper = pojo<String>().nonNull())
    }

    private fun socketFor(chainId: ChainId) = chainRegistry.getConnection(chainId).socketService

    private fun bindPartialFee(decoded: Any?): FeeResponse {
        val asStruct = decoded.castToStruct()

        return FeeResponse(bindNumber(asStruct["partialFee"]))
    }
}
