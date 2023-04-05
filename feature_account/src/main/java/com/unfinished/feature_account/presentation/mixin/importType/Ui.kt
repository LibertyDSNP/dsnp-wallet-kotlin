package com.unfinished.feature_account.presentation.mixin.importType

import io.novafoundation.nova.common.base.BaseFragment

fun BaseFragment<*>.setupImportTypeChooser(mixin: ImportTypeChooserMixin) {
    mixin.showChooserEvent.observeEvent {
        ImportTypeChooserBottomSheet(
            context = requireContext(),
            onChosen = it.onChosen,
            allowedSources = it.allowedTypes
        ).show()
    }
}
