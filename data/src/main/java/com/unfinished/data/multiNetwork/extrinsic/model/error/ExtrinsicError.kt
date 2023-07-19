package com.unfinished.data.multiNetwork.extrinsic.model.error

import androidx.annotation.StringRes
import com.unfinished.data.multiNetwork.runtime.binding.BlockHash

data class ExtrinsicError(
    val hash: BlockHash? = null,
    val error: String?,
)

fun ExtrinsicError.castToException(str_error: String? = ""): Throwable {
    return Exception(this.error ?: str_error)
}
