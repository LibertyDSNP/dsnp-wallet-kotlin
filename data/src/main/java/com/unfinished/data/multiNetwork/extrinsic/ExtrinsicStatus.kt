package com.unfinished.data.multiNetwork.extrinsic

import jp.co.soramitsu.fearless_utils.wsrpc.response.RpcResponse
import jp.co.soramitsu.fearless_utils.wsrpc.subscription.response.SubscriptionChange

sealed class ExtrinsicStatus(val extrinsicHash: String, val terminal: Boolean) {

    class Future(extrinsicHash: String) : ExtrinsicStatus(extrinsicHash, terminal = false)

    class Retracted(extrinsicHash: String) : ExtrinsicStatus(extrinsicHash, terminal = false)

    class Usurped(extrinsicHash: String) : ExtrinsicStatus(extrinsicHash, terminal = false)

    class Dropped(extrinsicHash: String) : ExtrinsicStatus(extrinsicHash, terminal = false)

    class Invalid(extrinsicHash: String) : ExtrinsicStatus(extrinsicHash, terminal = false)

    class Ready(extrinsicHash: String) : ExtrinsicStatus(extrinsicHash, terminal = false)

    class Broadcast(extrinsicHash: String) : ExtrinsicStatus(extrinsicHash, terminal = false)

    class InBlock(val blockHash: String, extrinsicHash: String) : ExtrinsicStatus(extrinsicHash, terminal = false)

    class Finalized(val blockHash: String, extrinsicHash: String) : ExtrinsicStatus(extrinsicHash, terminal = true)

    class Other(extrinsicHash: String) : ExtrinsicStatus(extrinsicHash, terminal = false)

}

private const val STATUS_FUTURE = "future"
private const val STATUS_READY = "ready"
private const val STATUS_BROADCAST = "broadcast"
private const val STATUS_IN_BLOCK = "inBlock"
private const val STATUS_RETRACTED = "retracted"
private const val STATUS_FINALITY_TIMEOUT = "finalityTimeout"
private const val STATUS_FINALIZED = "finalized"
private const val STATUS_USURPED = "usurped"
private const val STATUS_DROPPED = "dropped"
private const val STATUS_INVALID = "invalid"

fun SubscriptionChange.asExtrinsicStatus(extrinsicHash: String): ExtrinsicStatus {
    return when (val result = params.result) {
        STATUS_READY -> ExtrinsicStatus.Ready(extrinsicHash)
        is Map<*, *> -> when {
            STATUS_BROADCAST in result -> ExtrinsicStatus.Broadcast(extrinsicHash)
            STATUS_FUTURE in result -> ExtrinsicStatus.Future(extrinsicHash)
            STATUS_RETRACTED in result -> ExtrinsicStatus.Retracted(extrinsicHash)
            STATUS_USURPED in result -> ExtrinsicStatus.Usurped(extrinsicHash)
            STATUS_DROPPED in result -> ExtrinsicStatus.Dropped(extrinsicHash)
            STATUS_INVALID in result -> ExtrinsicStatus.Invalid(extrinsicHash)
            STATUS_IN_BLOCK in result -> ExtrinsicStatus.InBlock(
                extractBlockHash(
                    result,
                    STATUS_IN_BLOCK
                ), extrinsicHash
            )
            STATUS_FINALIZED in result -> ExtrinsicStatus.Finalized(
                extractBlockHash(
                    result,
                    STATUS_FINALIZED
                ), extrinsicHash
            )
            STATUS_FINALITY_TIMEOUT in result -> ExtrinsicStatus.Finalized(
                extractBlockHash(
                    result,
                    STATUS_FINALITY_TIMEOUT
                ), extrinsicHash
            )
            else -> ExtrinsicStatus.Other(extrinsicHash)
        }
        else -> unknownStructure()
    }
}
private fun extractBlockHash(map: Map<*, *>, key: String): String {
    return map[key] as? String ?: unknownStructure()
}

private fun unknownStructure(): Nothing = throw IllegalArgumentException("Unknown extrinsic status structure")
