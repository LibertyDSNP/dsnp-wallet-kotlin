package com.unfinished.data.multiNetwork.rpc.calls

import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.RuntimeRequest

object GetFinalizedHeadRequest : RuntimeRequest(
    method = "chain_getFinalizedHead",
    params = emptyList()
)
