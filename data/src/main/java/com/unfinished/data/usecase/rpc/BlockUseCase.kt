package com.unfinished.data.usecase.rpc

import com.unfinished.data.model.block.SignedBlock
import com.unfinished.data.multiNetwork.ChainRegistry
import com.unfinished.data.multiNetwork.chain.model.Chain
import com.unfinished.data.multiNetwork.getSocket
import com.unfinished.data.multiNetwork.rpc.RpcCalls
import com.unfinished.data.multiNetwork.rpc.calls.GetBlockHashRequest
import com.unfinished.data.multiNetwork.rpc.calls.GetBlockRequest
import com.unfinished.data.repository.event.EventsRepository
import jp.co.soramitsu.fearless_utils.wsrpc.executeAsync
import jp.co.soramitsu.fearless_utils.wsrpc.mappers.nonNull
import jp.co.soramitsu.fearless_utils.wsrpc.mappers.pojo
import java.math.BigInteger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BlockUseCase @Inject constructor(
    private val chainRegistry: ChainRegistry,
    private val eventsRepository: EventsRepository,
){
    suspend fun getBlockEvents(chain: Chain, blockHash: String? = null) =
        eventsRepository.getEventsInBlockForFrequency(chain.id, blockHash)

    suspend fun getGenesisHash(chain: Chain): String {
        val blockRequest = GetBlockHashRequest(BigInteger.ZERO)
        return chainRegistry.getSocket(chain.id)
            .executeAsync(blockRequest, mapper = pojo<String>().nonNull())
    }

    suspend fun getBlock(chain: Chain, hash: String? = null): SignedBlock {
        val blockRequest = GetBlockRequest(hash)
        return chainRegistry.getSocket(chain.id)
            .executeAsync(blockRequest, mapper = pojo<SignedBlock>().nonNull())
    }
}
