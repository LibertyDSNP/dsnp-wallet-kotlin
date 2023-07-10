package com.unfinished.common.mixin.impl

import com.unfinished.common.base.BaseFragment
import com.unfinished.common.base.BaseViewModel
import com.unfinished.common.mixin.api.Browserable
import com.unfinished.common.utils.showBrowser

fun <T> BaseFragment<T>.observeBrowserEvents(viewModel: T) where T : BaseViewModel, T : Browserable {
    viewModel.openBrowserEvent.observeEvent(this::showBrowser)
}
