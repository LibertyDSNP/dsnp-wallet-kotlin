package io.novafoundation.nova.common.data.network.runtime.calls

import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.RuntimeRequest
import org.json.JSONArray

class GetChildStateRequest(
    storageKey: String,
    childKey: String
) : RuntimeRequest(
    method = "childstate_getStorage",
    params = listOf(childKey, storageKey)
)

class StateGetRuntimeVersion : RuntimeRequest("state_getRuntimeVersion", listOf())