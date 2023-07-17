package com.unfinished.account.presentation.mixin

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AccountNameChooserMixin {

    val nameState: StateFlow<State>

    fun nameChanged(newName: String)

    interface Presentation : AccountNameChooserMixin {

        val nameValid: Flow<Boolean>
    }

    sealed class State {

        object NoInput : State()

        class Input(val value: String) : State()
    }
}

interface WithAccountNameChooserMixin {

    val accountNameChooser: AccountNameChooserMixin
}
