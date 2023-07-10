package com.unfinished.common.data.network.runtime.calls

import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.RuntimeRequest

class GetMsaId() : RuntimeRequest(
    method = "currentMsaIdentifierMaximum",
    params = listOfNotNull()
)
