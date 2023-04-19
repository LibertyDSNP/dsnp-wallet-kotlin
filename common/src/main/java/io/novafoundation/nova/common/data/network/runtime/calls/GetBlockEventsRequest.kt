package io.novafoundation.nova.common.data.network.runtime.calls

import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.RuntimeRequest

class GetBlockEventsRequest(blockHash: String? = null) : RuntimeRequest(
    method = "chain_getEvents",
    params = listOfNotNull(
        blockHash
    )
)
