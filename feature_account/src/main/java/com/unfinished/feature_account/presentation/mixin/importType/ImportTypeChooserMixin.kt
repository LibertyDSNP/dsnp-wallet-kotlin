package com.unfinished.feature_account.presentation.mixin.importType

import androidx.lifecycle.LiveData
import com.unfinished.feature_account.presentation.model.account.add.SecretType
import com.unfinished.common.utils.Event

interface ImportTypeChooserMixin {

    class Payload(
        val allowedTypes: Set<SecretType> = SecretType.values().toSet(),
        val onChosen: (SecretType) -> Unit
    )

    val showChooserEvent: LiveData<Event<Payload>>

    interface Presentation : ImportTypeChooserMixin {

        fun showChooser(payload: Payload)
    }
}
