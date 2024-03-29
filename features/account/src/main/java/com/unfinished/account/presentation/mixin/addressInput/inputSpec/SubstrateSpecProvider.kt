package com.unfinished.account.presentation.mixin.addressInput.inputSpec

import android.graphics.drawable.Drawable
import com.unfinished.common.address.AddressIconGenerator
import com.unfinished.runtime.util.isValidSS58Address
import jp.co.soramitsu.fearless_utils.ss58.SS58Encoder.toAccountId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class SubstrateSpecProvider(
    private val addressIconGenerator: AddressIconGenerator,
) : AddressInputSpecProvider {

    override val spec: Flow<AddressInputSpec> = flowOf(Spec())

    private inner class Spec : AddressInputSpec {

        override fun isValidAddress(input: String): Boolean {
            return input.isValidSS58Address()
        }

        override suspend fun generateIcon(input: String): Result<Drawable> {
            return runCatching {
                addressIconGenerator.createAddressIcon(
                    accountId = input.toAccountId(),
                    sizeInDp = AddressIconGenerator.SIZE_MEDIUM,
                    backgroundColorRes = AddressIconGenerator.BACKGROUND_TRANSPARENT
                )
            }
        }
    }
}
