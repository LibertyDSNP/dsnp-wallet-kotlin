package com.unfinished.runtime.network.runtime.calls

import com.unfinished.runtime.network.runtime.binding.BlockNumber
import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.RuntimeRequest

class GetBlockHashRequest(blockNumber: BlockNumber?) : RuntimeRequest(
    method = "chain_getBlockHash",
    params = listOfNotNull(
        blockNumber
    )
)
