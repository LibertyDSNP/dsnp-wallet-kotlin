package com.unfinished.account.presentation.action

import android.content.Context
import androidx.lifecycle.lifecycleScope
import com.unfinished.common.base.BaseFragment
import com.unfinished.common.base.BaseViewModel
import com.unfinished.common.mixin.impl.observeBrowserEvents

fun <T> BaseFragment<T>.setupExternalActions(viewModel: T) where T : BaseViewModel, T : ExternalActions {
    setupExternalActions(viewModel) { context, payload ->
        ExternalActionsSheet(
            context,
            payload,
            viewModel::copyAddressClicked,
            viewModel::viewExternalClicked
        )
    }
}

inline fun <T> BaseFragment<T>.setupExternalActions(
    viewModel: T,
    crossinline customSheetCreator: suspend (Context, ExternalActions.Payload) -> ExternalActionsSheet,
) where T : BaseViewModel, T : ExternalActions {
    observeBrowserEvents(viewModel)

    viewModel.showExternalActionsEvent.observeEvent {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            customSheetCreator(requireContext(), it).show()
        }
    }
}

fun <T> T.copyAddressClicked(address: String) where T : BaseViewModel, T : ExternalActions {
    copyAddress(address, ::showMessage)
}
