package com.unfinished.data.multiNetwork.extrinsic.service

import com.unfinished.data.util.ext.orZero
import com.unfinished.data.multiNetwork.extrinsic.model.FeeResponse
import com.unfinished.data.model.account.MetaAccount
import com.unfinished.data.model.account.accountIdIn
import com.unfinished.data.model.event.EventType
import com.unfinished.data.util.ext.tip
import com.unfinished.data.multiNetwork.chain.model.Chain
import com.unfinished.data.multiNetwork.chain.model.ChainId
import com.unfinished.data.multiNetwork.extrinsic.ExtrinsicBuilderFactory
import com.unfinished.data.multiNetwork.extrinsic.ExtrinsicStatus
import com.unfinished.data.multiNetwork.extrinsic.create
import com.unfinished.data.multiNetwork.extrinsic.model.error.ExtrinsicError
import com.unfinished.data.multiNetwork.rpc.RpcCalls
import com.unfinished.data.multiNetwork.runtime.binding.checkIfExtrinsicFailed
import com.unfinished.data.repository.event.EventsRepository
import com.unfinished.data.signer.SignerProvider
import com.unfinished.data.util.ext.takeWhileInclusive
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.fromHex
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.generics.Extrinsic
import jp.co.soramitsu.fearless_utils.runtime.extrinsic.ExtrinsicBuilder
import jp.co.soramitsu.fearless_utils.wsrpc.exception.RpcException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.firstOrNull
import java.math.BigInteger

typealias EncodedExtrinsic = (String) -> Unit
typealias ResultBinder<T> = (String , List<EventType>) -> T
typealias ErrorBinder<T> = (ExtrinsicError) -> T
class RealExtrinsicService(
    private val rpcCalls: RpcCalls,
    private val extrinsicBuilderFactory: ExtrinsicBuilderFactory,
    private val signerProvider: SignerProvider,
    private val eventsRepository: EventsRepository,
) : ExtrinsicService {
    override suspend fun <T> submitAndWatchExtrinsic(
        metaAccount: MetaAccount,
        chain: Chain,
        extrinsicCall: suspend ExtrinsicBuilder.() -> Unit,
        encodedExtrinsic: EncodedExtrinsic,
        failure: ErrorBinder<T>,
        result: ResultBinder<T>,
    ){
        submitAndWatchExtrinsicAnySuitableWallet(chain, metaAccount, encodedExtrinsic, extrinsicCall)
            .catch {
                failure(
                    ExtrinsicError(
                        error = (it as RpcException).message
                    )
                )
            }
            .filterIsInstance<ExtrinsicStatus.Finalized>()
            .firstOrNull()?.let { extrinsicStatus ->
                val events = eventsRepository.getEventsInBlockForFrequency(chain.id, extrinsicStatus.blockHash)
                events.onSuccess { eventList ->
                    eventList.checkIfExtrinsicFailed()?.let { dispatchError ->
                        failure(
                            ExtrinsicError(
                                hash = extrinsicStatus.blockHash,
                                error = dispatchError.error?.second?.name
                            )
                        )
                    } ?: kotlin.run {
                        result( extrinsicStatus.blockHash, eventList)
                    }
                }.onFailure {
                    failure(
                        ExtrinsicError(
                            hash = extrinsicStatus.blockHash,
                            error = it.message
                        )
                    )
                }
            }
    }
    override suspend fun submitAndWatchExtrinsicAnySuitableWallet(
        chain: Chain,
        metaAccount: MetaAccount,
        encodedExtrinsic: EncodedExtrinsic,
        extrinsicCall: suspend ExtrinsicBuilder.() -> Unit,
    ): Flow<ExtrinsicStatus> {
        val extrinsic = buildExtrinsic(chain, metaAccount, extrinsicCall)
        encodedExtrinsic(extrinsic)
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
        fromExtrinsic: suspend ExtrinsicBuilder.() -> Unit,
    ): BigInteger {
        val extrinsicBuilder = extrinsicBuilderFactory.createForFee(chain)
        extrinsicBuilder.fromExtrinsic()
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
        extrinsicCall: suspend ExtrinsicBuilder.() -> Unit,
    ): String {
        val signer = signerProvider.signerFor(metaAccount)
        val accountId =  metaAccount.accountIdIn(chain)!!
        val extrinsicBuilder = extrinsicBuilderFactory.create(chain, signer, accountId)
        extrinsicBuilder.extrinsicCall()
        return extrinsicBuilder.build(useBatchAll = true)
    }
}
