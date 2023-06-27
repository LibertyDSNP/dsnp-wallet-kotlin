package com.unfinished.dsnp_wallet_kotlin.ui.recovery.uimodel

data class RecoveryPhraseUiModel(
    val seedKeys: List<SeedKey> = listOf()
)

data class SeedKey(
    val prefix: String,
    val key: String
)