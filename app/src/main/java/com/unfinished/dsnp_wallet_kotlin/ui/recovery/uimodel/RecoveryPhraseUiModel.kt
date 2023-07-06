package com.unfinished.dsnp_wallet_kotlin.ui.recovery.uimodel

data class RecoveryPhraseUiModel(
    val seedKeys: List<SeedKey> = emptyList(),
    val currentRandomSeedKeys: List<SeedKey> = emptyList(),
    val currentSeedGuesses: List<SeedKey?> = mutableListOf<SeedKey?>().apply {
        seedKeys.forEach { add(null) }
    },
    val seedKeyState: SeedKeyState = SeedKeyState.Init
) {
    val continueEnabled: Boolean
        get() {
            val guessSize = currentSeedGuesses.filterNotNull().size

            return seedKeys.size == guessSize
        }

    val mnemonicString: String
        get() = seedKeys.joinToString(separator = " ") { it.key }

    fun createEmptyGuesses(): List<SeedKey?> = mutableListOf<SeedKey?>().apply {
        seedKeys.forEach { add(null) }
    }
}

data class SeedKey(
    val prefix: String,
    val key: String
)

enum class SeedKeyState {
    Init, NotValid, Finish, Verifying
}