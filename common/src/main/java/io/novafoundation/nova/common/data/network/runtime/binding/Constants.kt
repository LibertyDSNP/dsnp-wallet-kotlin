package io.novafoundation.nova.common.data.network.runtime.binding

import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.fromByteArrayOrNull
import jp.co.soramitsu.fearless_utils.runtime.metadata.module.Constant
import java.math.BigInteger

@HelperBinding
fun bindNumberConstant(
    constant: Constant,
    runtime: RuntimeSnapshot
): BigInteger = bindNullableNumberConstant(constant, runtime) ?: incompatible()

@HelperBinding
fun bindNullableNumberConstant(
    constant: Constant,
    runtime: RuntimeSnapshot
): BigInteger? {
    val decoded = constant.type?.fromByteArrayOrNull(runtime, constant.value) ?: incompatible()

    return decoded as BigInteger?
}
