package com.unfinished.runtime.network.runtime.calls

import com.unfinished.runtime.util.Modules
import jp.co.soramitsu.fearless_utils.runtime.AccountId
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.composite.DictEnum
import jp.co.soramitsu.fearless_utils.runtime.extrinsic.ExtrinsicBuilder
import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.RuntimeRequest
import java.math.BigInteger

class GetCurrentMsaIdentifierMaximum(
    storageKey: String
) : RuntimeRequest(
    method = "currentMsaIdentifierMaximum",
    params = listOf(storageKey)
)

class GetStateRequest(
    storageKey: String
) : RuntimeRequest(
    method = "state_getStorage",
    params = listOf(storageKey)
)


fun ExtrinsicBuilder.transferCall(
    destAccount: AccountId,
    amount: BigInteger
): ExtrinsicBuilder {
    return call(
        moduleName = Modules.BALANCES,
        callName = "transfer",
        arguments = mapOf(
            "dest" to DictEnum.Entry(
                name = "Id",
                value = destAccount
            ),
            "value" to amount
        )
    )
}


