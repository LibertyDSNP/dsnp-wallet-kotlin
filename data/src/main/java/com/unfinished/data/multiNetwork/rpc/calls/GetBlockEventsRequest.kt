package com.unfinished.data.multiNetwork.rpc.calls

import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.RuntimeRequest

class GetBlockEventsRequest(blockHash: String? = null) : RuntimeRequest(
    method = "chain_getEvents",
    params = listOfNotNull(
        blockHash
    )
)
