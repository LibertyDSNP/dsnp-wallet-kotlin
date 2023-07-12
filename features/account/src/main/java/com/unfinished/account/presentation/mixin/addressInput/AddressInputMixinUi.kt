package com.unfinished.account.presentation.mixin.addressInput

import androidx.lifecycle.lifecycleScope
import com.unfinished.common.base.BaseFragment
import com.unfinished.common.utils.bindTo

fun BaseFragment<*>.setupAddressInput(
    mixin: AddressInputMixin,
    view: AddressInputField
) = with(view) {
    content.bindTo(mixin.inputFlow, lifecycleScope)

    onScanClicked { mixin.scanClicked() }
    onPasteClicked { mixin.pasteClicked() }
    onClearClicked { mixin.clearClicked() }
    onMyselfClicked { mixin.myselfClicked() }

    mixin.state.observe(::setState)
}
