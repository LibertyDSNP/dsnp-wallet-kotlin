package com.unfinished.common.mixin.api

import androidx.lifecycle.LiveData
import com.unfinished.common.utils.Event
import com.unfinished.common.validation.ValidationStatus

sealed class ValidationFailureUi {

    class Default(
        val level: ValidationStatus.NotValid.Level,
        val title: String,
        val message: String?,
        val confirmWarning: Action,
    ) : ValidationFailureUi()

    class Custom(val payload: CustomDialogDisplayer.Payload) : ValidationFailureUi()
}

interface Validatable {
    val validationFailureEvent: LiveData<Event<ValidationFailureUi>>
}
