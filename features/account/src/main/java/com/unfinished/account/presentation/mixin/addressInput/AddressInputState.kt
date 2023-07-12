package com.unfinished.account.presentation.mixin.addressInput

import android.graphics.drawable.Drawable

class AddressInputState(
    val iconState: IdenticonState,
    val pasteShown: Boolean,
    val scanShown: Boolean,
    val clearShown: Boolean,
    val myselfShown: Boolean,
) {

    sealed class IdenticonState {

        object Placeholder : IdenticonState()

        class Address(val drawable: Drawable) : IdenticonState()
    }
}
