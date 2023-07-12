package com.unfinished.account.presentation.mnemonic.confirm

data class MnemonicWord(
    val id: String,
    val content: String,
    var indexDisplay: String?,
    val removed: Boolean
)
