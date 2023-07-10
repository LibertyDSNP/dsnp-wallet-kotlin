package com.unfinished.common.mixin.impl

import androidx.lifecycle.MutableLiveData
import com.unfinished.common.base.BaseFragmentMixin
import com.unfinished.common.base.BaseViewModel
import com.unfinished.common.mixin.api.CustomDialogDisplayer
import com.unfinished.common.utils.Event
import com.unfinished.common.utils.themed
import com.unfinished.common.view.dialog.dialog

class CustomDialogProvider : CustomDialogDisplayer.Presentation {

    override val showCustomDialog = MutableLiveData<Event<CustomDialogDisplayer.Payload>>()

    override fun displayDialog(payload: CustomDialogDisplayer.Payload) {
        showCustomDialog.postValue(Event(payload))
    }
}

fun <V> BaseFragmentMixin<V>.setupCustomDialogDisplayer(
    viewModel: V,
) where V : BaseViewModel, V : CustomDialogDisplayer {
    viewModel.showCustomDialog.observeEvent {
        displayDialogFor(it)
    }
}

fun BaseFragmentMixin<*>.displayDialogFor(payload: CustomDialogDisplayer.Payload) {
    val themedContext = payload.customStyle?.let(providedContext::themed) ?: providedContext

    dialog(themedContext) {
        setTitle(payload.title)
        setMessage(payload.message)

        setPositiveButton(payload.okAction.title) { _, _ ->
            payload.okAction.action()
        }

        payload.cancelAction?.let { negativeAction ->
            setNegativeButton(negativeAction.title) { _, _ ->
                negativeAction.action()
            }
        }
    }
}
