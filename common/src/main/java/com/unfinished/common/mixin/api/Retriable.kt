package com.unfinished.common.mixin.api

import androidx.lifecycle.MutableLiveData
import com.unfinished.common.utils.Event

typealias Action = () -> Unit

class RetryPayload(
    val title: String,
    val message: String,
    val onRetry: Action,
    val onCancel: Action? = null
)

interface Retriable {

    val retryEvent: MutableLiveData<Event<RetryPayload>>
}
