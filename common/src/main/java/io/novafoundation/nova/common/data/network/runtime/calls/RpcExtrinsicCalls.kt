package io.novafoundation.nova.common.data.network.runtime.calls

import io.novafoundation.nova.common.utils.Modules
import jp.co.soramitsu.fearless_utils.runtime.AccountId
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.composite.DictEnum
import jp.co.soramitsu.fearless_utils.runtime.extrinsic.ExtrinsicBuilder
import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.RuntimeRequest
import java.math.BigDecimal
import java.math.BigInteger

class GetStateRequest(
    storageKey: String
) : RuntimeRequest(
    method = "state_getStorage",
    params = listOf(storageKey)
)

class GetPaymentQueryInfoRequest(
    extrinsic: String
) : RuntimeRequest(
    method = "payment_queryInfo",
    params = listOf(extrinsic)
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

fun ExtrinsicBuilder.createMsa(): ExtrinsicBuilder {
    return call(
        moduleName = "Msa",
        callName = "create",
        arguments = mapOf()
    )
}

fun ExtrinsicBuilder.addKeyToMsa(
    key: String,
    proof: String,
    addKeyPayload: AddKeyPayload
): ExtrinsicBuilder {
    return call(
        moduleName = "Msa",
        callName = "addKeyToMsa",
        arguments = mapOf(
            "key" to key,
            "proof" to proof,
            "addKeyPayload" to addKeyPayload
        )
    )
}

data class AddKeyPayload(
    val msaId: Int,
    val nonce: BigInteger,
)

