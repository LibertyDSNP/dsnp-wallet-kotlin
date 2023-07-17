package com.unfinished.account.presentation.mixin

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.unfinished.common.base.BaseFragment
import com.unfinished.common.base.BaseViewModel
import com.unfinished.common.utils.observeInLifecycle
import com.unfinished.common.utils.onTextChanged
import com.unfinished.common.utils.setVisible
import com.unfinished.common.view.InputField

fun <V> BaseFragment<V>.setupAccountNameChooserUi(
    viewModel: V,
    ui: InputField,
    additionalViewsToControlVisibility: List<View> = emptyList(),
) where V : BaseViewModel, V : WithAccountNameChooserMixin {
    setupAccountNameChooserUi(viewModel, ui, viewLifecycleOwner, additionalViewsToControlVisibility)
}

fun setupAccountNameChooserUi(
    viewModel: WithAccountNameChooserMixin,
    ui: InputField,
    owner: LifecycleOwner,
    additionalViewsToControlVisibility: List<View> = emptyList(),
) {
    ui.content.onTextChanged {
        viewModel.accountNameChooser.nameChanged(it)
    }

    viewModel.accountNameChooser.nameState.observeInLifecycle(owner.lifecycleScope) { state ->
        val isVisible = state is AccountNameChooserMixin.State.Input

        ui.setVisible(isVisible)
        additionalViewsToControlVisibility.forEach { it.setVisible(isVisible) }

        if (state is AccountNameChooserMixin.State.Input) {
            if (state.value != ui.content.text.toString()) {
                ui.content.setText(state.value)
            }
        }
    }
}
