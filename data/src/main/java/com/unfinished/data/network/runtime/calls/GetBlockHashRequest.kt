package com.unfinished.data.network.runtime.calls

import com.unfinished.data.network.runtime.binding.BlockNumber
import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.RuntimeRequest

class GetBlockHashRequest(blockNumber: BlockNumber?) : RuntimeRequest(
    method = "chain_getBlockHash",
    params = listOfNotNull(
        blockNumber
    )
)
