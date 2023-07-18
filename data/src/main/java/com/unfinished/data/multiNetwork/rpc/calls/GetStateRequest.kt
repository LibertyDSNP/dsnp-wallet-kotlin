package com.unfinished.data.multiNetwork.rpc.calls

import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.RuntimeRequest

class GetStateRequest(
    storageKey: String
) : RuntimeRequest(
    method = "state_getStorage",
    params = listOf(storageKey)
)

class GetChildStateRequest(
    storageKey: String,
    childKey: String
) : RuntimeRequest(
    method = "childstate_getStorage",
    params = listOf(childKey, storageKey)
)

class StateGetRuntimeVersion : RuntimeRequest("state_getRuntimeVersion", listOf())