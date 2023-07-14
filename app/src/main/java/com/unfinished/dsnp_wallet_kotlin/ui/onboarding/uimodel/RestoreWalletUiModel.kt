package com.unfinished.dsnp_wallet_kotlin.ui.onboarding.uimodel

data class RestoreWalletUiModel(
    val recoveryPhrase: String = "",
    val state: State = State.Init
) {
    enum class State {
        Init, Loading, Error
    }

    val continueEnabled: Boolean
        get() = recoveryPhrase.split(" ").filter { it.isNotBlank() }.size == 12
}