package com.unfinished.feature_account.presentation.mixin.addressInput

import androidx.lifecycle.lifecycleScope
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.utils.bindTo

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
