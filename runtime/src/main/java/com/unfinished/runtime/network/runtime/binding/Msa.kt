package com.unfinished.runtime.network.runtime.binding

import com.unfinished.runtime.util.decodeValue
import com.unfinished.runtime.util.msa
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



