package com.unfinished.common.data.network.runtime.binding

import com.unfinished.common.data.network.runtime.model.event.*
import com.unfinished.common.utils.decodeValue
import com.unfinished.common.utils.msa
import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import jp.co.soramitsu.fearless_utils.runtime.metadata.storage
import java.lang.Exception
import java.math.BigInteger

@UseCaseBinding
fun bindMsaRecordsForFrequency(
    scale: String?,
    runtime: RuntimeSnapshot,
): Result<BigInteger> {
    return runCatching {
        if (scale != null)
            runtime.metadata.msa().storage("PublicKeyToMsaId").decodeValue(scale, runtime).toString().toBigInteger()
        else
            throw Exception("No Msa Id found")
    }
}



