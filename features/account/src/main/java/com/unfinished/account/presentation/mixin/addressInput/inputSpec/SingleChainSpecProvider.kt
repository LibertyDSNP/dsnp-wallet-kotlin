package com.unfinished.account.presentation.mixin.addressInput.inputSpec

import android.graphics.drawable.Drawable
import com.unfinished.common.address.AddressIconGenerator
import com.unfinished.data.util.ext.accountIdOf
import com.unfinished.data.util.ext.isValidAddress
import com.unfinished.data.multiNetwork.chain.model.Chain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SingleChainSpecProvider(
    private val addressIconGenerator: AddressIconGenerator,
    targetChain: Flow<Chain>,
) : AddressInputSpecProvider {

    override val spec: Flow<AddressInputSpec> = targetChain.map { Spec(it) }

    private inner class Spec(private val targetChain: Chain) : AddressInputSpec {

        override fun isValidAddress(input: String): Boolean {
            return targetChain.isValidAddress(input)
        }

        override suspend fun generateIcon(input: String): Result<Drawable> {
            return runCatching {
                require(targetChain.isValidAddress(input))

                addressIconGenerator.createAddressIcon(
                    accountId = targetChain.accountIdOf(address = input),
                    sizeInDp = AddressIconGenerator.SIZE_MEDIUM,
                    backgroundColorRes = AddressIconGenerator.BACKGROUND_TRANSPARENT
                )
            }
        }
    }
}
