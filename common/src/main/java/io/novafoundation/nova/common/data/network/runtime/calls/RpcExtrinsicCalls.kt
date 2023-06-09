package io.novafoundation.nova.common.data.network.runtime.calls

import io.novafoundation.nova.common.utils.Modules
import jp.co.soramitsu.fearless_utils.runtime.AccountId
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.composite.DictEnum
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.composite.Struct
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
        moduleName = Modules.MSA,
        callName = "create",
        arguments = mapOf()
    )
}

fun ExtrinsicBuilder.addPublicKeyToMsa(
    msaOwnerPublicKey: ByteArray?,
    msaOwnerProof: DictEnum.Entry<*>,
    newKeyOwnerProof: DictEnum.Entry<*>,
    addKeyPayload: Struct.Instance,
): ExtrinsicBuilder {
    return call(
        moduleName = "Msa",
        callName = "add_public_key_to_msa",
        arguments = mapOf(
            "msa_owner_public_key" to msaOwnerPublicKey,
            "msa_owner_proof" to msaOwnerProof,
            "new_key_owner_proof" to newKeyOwnerProof,
            "add_key_payload" to addKeyPayload
        )
    )
}

data class AddKeyPayload(
    val msaId: Any,
    val expiration: Any,
    val newPublicKey: Any,
)

