package com.unfinished.account.presentation.mixin.importType

import androidx.lifecycle.MutableLiveData
import com.unfinished.common.utils.Event

class ImportTypeChooserProvider : ImportTypeChooserMixin.Presentation {

    override fun showChooser(payload: ImportTypeChooserMixin.Payload) {
        showChooserEvent.value = Event(payload)
    }

    override val showChooserEvent = MutableLiveData<Event<ImportTypeChooserMixin.Payload>>()
}
