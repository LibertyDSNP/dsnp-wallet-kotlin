package com.unfinished.data.multiNetwork.runtime

import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.RuntimeRequest

object SubscribeRuntimeVersionRequest : RuntimeRequest(
    method = "state_subscribeRuntimeVersion",
    params = listOf()
)
