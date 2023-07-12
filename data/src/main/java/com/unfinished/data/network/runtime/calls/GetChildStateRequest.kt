package com.unfinished.data.network.runtime.calls

import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.RuntimeRequest

class GetChildStateRequest(
    storageKey: String,
    childKey: String
) : RuntimeRequest(
    method = "childstate_getStorage",
    params = listOf(childKey, storageKey)
)

class StateGetRuntimeVersion : RuntimeRequest("state_getRuntimeVersion", listOf())