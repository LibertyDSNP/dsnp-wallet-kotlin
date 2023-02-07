package com.unfinished.feature_account.presentation.mixin.addressInput

import com.unfinished.feature_account.presentation.mixin.addressInput.inputSpec.AddressInputSpec
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface AddressInputMixin {

    suspend fun getInputSpec(): AddressInputSpec

    val inputFlow: MutableStateFlow<String>

    val state: Flow<AddressInputState>

    fun pasteClicked()

    fun clearClicked()

    fun scanClicked()

    fun myselfClicked()
}

suspend fun AddressInputMixin.isAddressValid(input: String) = getInputSpec().isValidAddress(input)
