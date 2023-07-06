package com.unfinished.dsnp_wallet_kotlin.ui.recovery.uimodel

data class RecoveryPhraseUiModel(
    val seedKeys: List<SeedKey> = emptyList(),
    val currentSeedGuesses: List<SeedKey> = emptyList(),
    val currentRandomSeedKeys: List<SeedKey> = emptyList(),
    val seedKeyState: SeedKeyState = SeedKeyState.Init
) {
    val continueEnabled: Boolean
        get() = currentSeedGuesses.size == seedKeys.size

    val mnemonicString: String
        get() = seedKeys.joinToString(separator = " ") { it.key }
}

data class SeedKey(
    val prefix: String,
    val key: String
)

enum class SeedKeyState {
    Init, NotValid, Finish, Verifying
}