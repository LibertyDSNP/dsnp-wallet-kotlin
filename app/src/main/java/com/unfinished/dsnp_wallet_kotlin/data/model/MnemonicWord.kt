package com.unfinished.dsnp_wallet_kotlin.data.model

data class MnemonicWord(
    var id: String,
    val content: String,
    var indexDisplay: String?,
    val removed: Boolean
)