package io.novafoundation.nova.runtime.sign

import android.os.Parcelable
import com.google.gson.Gson


class PolkadotJsSignRequest(val id: String, val payload: SignerPayload)

sealed class SignerPayload {

    abstract val address: String

    class Json(
        override val address: String,
        val blockHash: String,
        val blockNumber: String,
        val era: String,
        val genesisHash: String,
        val method: String,
        val nonce: String,
        val specVersion: String,
        val tip: String,
        val transactionVersion: String,
        val signedExtensions: List<String>,
        val version: Int
    ) : SignerPayload()

    class Raw(
        val data: String,
        override val address: String,
        val type: String?
    ) : SignerPayload()
}

fun SignerPayload.maybeSignExtrinsic() = this as? SignerPayload.Json?

fun mapRawPayloadToSignerPayloadJSON(
    raw: Any?,
    gson: Gson
): SignerPayload.Json? {
    val tree = gson.toJsonTree(raw)

    return runCatching {
        gson.fromJson(tree, SignerPayload.Json::class.java)
    }.getOrNull()
}

fun mapRawPayloadToSignerPayloadRaw(
    raw: Any?,
    gson: Gson
): SignerPayload.Raw? {
    val tree = gson.toJsonTree(raw)

    return runCatching {
        gson.fromJson(tree, SignerPayload.Raw::class.java)
    }.getOrNull()
}
