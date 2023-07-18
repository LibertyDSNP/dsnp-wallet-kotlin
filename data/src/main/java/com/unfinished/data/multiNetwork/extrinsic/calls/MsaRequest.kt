package com.unfinished.data.multiNetwork.extrinsic.calls

import com.unfinished.data.util.Modules
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.composite.DictEnum
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.composite.Struct
import jp.co.soramitsu.fearless_utils.runtime.extrinsic.ExtrinsicBuilder
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

fun ExtrinsicBuilder.deletePublicKeyToMsa(
    publicKeyToDelete: ByteArray?
): ExtrinsicBuilder {
    return call(
        moduleName = "Msa",
        callName = "delete_msa_public_key",
        arguments = mapOf(
            "public_key_to_delete" to publicKeyToDelete,
        )
    )
}

fun ExtrinsicBuilder.retireMsa(): ExtrinsicBuilder {
    return call(
        moduleName = "Msa",
        callName = "retire_msa",
        arguments = mapOf()
    )
}
fun ExtrinsicBuilder.createProvider(providerName: ByteArray): ExtrinsicBuilder {
    return call(
        moduleName = "Msa",
        callName = "create_provider",
        arguments = mapOf(
            "provider_name" to providerName,
        )
    )
}
