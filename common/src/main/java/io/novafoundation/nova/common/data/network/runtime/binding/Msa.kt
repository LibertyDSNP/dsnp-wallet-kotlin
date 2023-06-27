package io.novafoundation.nova.common.data.network.runtime.binding

import io.novafoundation.nova.common.data.network.runtime.model.event.*
import io.novafoundation.nova.common.utils.Modules
import io.novafoundation.nova.common.utils.decodeValue
import io.novafoundation.nova.common.utils.msa
import io.novafoundation.nova.common.utils.system
import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.composite.Struct
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.fromHex
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



