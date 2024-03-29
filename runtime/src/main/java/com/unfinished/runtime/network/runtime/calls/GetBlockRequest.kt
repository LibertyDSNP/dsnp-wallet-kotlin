package com.unfinished.runtime.network.runtime.calls

import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.RuntimeRequest

class GetBlockRequest(blockHash: String? = null) : RuntimeRequest(
    method = "chain_getBlock",
    params = listOfNotNull(
        blockHash
    )
)
