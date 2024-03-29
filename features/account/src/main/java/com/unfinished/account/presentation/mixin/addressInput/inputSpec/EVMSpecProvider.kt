package com.unfinished.account.presentation.mixin.addressInput.inputSpec

import android.graphics.drawable.Drawable
import com.unfinished.common.address.AddressIconGenerator
import jp.co.soramitsu.fearless_utils.extensions.asEthereumAddress
import jp.co.soramitsu.fearless_utils.extensions.isValid
import jp.co.soramitsu.fearless_utils.extensions.toAccountId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class EVMSpecProvider(
    private val addressIconGenerator: AddressIconGenerator
) : AddressInputSpecProvider {

    override val spec: Flow<AddressInputSpec> = flowOf(Spec())

    private inner class Spec : AddressInputSpec {

        override fun isValidAddress(input: String): Boolean {
            return input.asEthereumAddress().isValid()
        }

        override suspend fun generateIcon(input: String): Result<Drawable> {
            return runCatching {
                addressIconGenerator.createAddressIcon(
                    accountId = input.asEthereumAddress().toAccountId().value,
                    sizeInDp = AddressIconGenerator.SIZE_MEDIUM,
                    backgroundColorRes = AddressIconGenerator.BACKGROUND_TRANSPARENT
                )
            }
        }
    }
}
