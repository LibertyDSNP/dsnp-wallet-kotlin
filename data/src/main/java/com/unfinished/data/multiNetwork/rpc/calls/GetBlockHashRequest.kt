package com.unfinished.data.multiNetwork.rpc.calls

import com.unfinished.data.multiNetwork.runtime.binding.BlockNumber
import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.RuntimeRequest

class GetBlockHashRequest(blockNumber: BlockNumber?) : RuntimeRequest(
    method = "chain_getBlockHash",
    params = listOfNotNull(
        blockNumber
    )
)
