package com.unfinished.data.multiNetwork.rpc.calls

import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.RuntimeRequest

class FeeCalculationRequest(extrinsicInHex: String) : RuntimeRequest(
    method = "payment_queryInfo",
    params = listOf(extrinsicInHex)
)