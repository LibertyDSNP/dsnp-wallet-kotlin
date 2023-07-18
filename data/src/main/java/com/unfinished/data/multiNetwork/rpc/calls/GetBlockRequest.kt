package com.unfinished.data.multiNetwork.rpc.calls

import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.RuntimeRequest

class GetBlockRequest(blockHash: String? = null) : RuntimeRequest(
    method = "chain_getBlock",
    params = listOfNotNull(
        blockHash
    )
)
