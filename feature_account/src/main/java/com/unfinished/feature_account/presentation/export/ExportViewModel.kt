package com.unfinished.feature_account.presentation.export

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.unfinished.common.base.BaseViewModel
import com.unfinished.common.utils.Event

abstract class ExportViewModel : BaseViewModel() {

    private val _exportEvent = MutableLiveData<Event<String>>()
    val exportEvent: LiveData<Event<String>> = _exportEvent

    protected fun exportText(text: String) {
        _exportEvent.value = Event(text)
    }
}
