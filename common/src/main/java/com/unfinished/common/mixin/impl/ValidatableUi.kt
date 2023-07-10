package com.unfinished.common.mixin.impl

import android.content.Context
import com.unfinished.common.base.BaseFragmentMixin
import com.unfinished.common.mixin.api.Validatable
import com.unfinished.common.mixin.api.ValidationFailureUi
import com.unfinished.common.validation.DefaultFailureLevel
import com.unfinished.common.view.dialog.errorDialog
import com.unfinished.common.view.dialog.warningDialog

fun BaseFragmentMixin<*>.observeValidations(
    viewModel: Validatable,
    dialogContext: Context = providedContext
) {
    viewModel.validationFailureEvent.observeEvent {
        when (it) {
            is ValidationFailureUi.Default -> {
                val level = it.level

                when {
                    level >= DefaultFailureLevel.ERROR -> errorDialog(dialogContext) {
                        setTitle(it.title)
                        setMessage(it.message)
                    }
                    level >= DefaultFailureLevel.WARNING -> warningDialog(
                        context = dialogContext,
                        onConfirm = it.confirmWarning
                    ) {
                        setTitle(it.title)
                        setMessage(it.message)
                    }
                }
            }
            is ValidationFailureUi.Custom -> displayDialogFor(it.payload)
        }
    }
}
