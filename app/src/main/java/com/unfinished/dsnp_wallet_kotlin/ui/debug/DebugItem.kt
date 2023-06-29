package com.unfinished.dsnp_wallet_kotlin.ui.debug

data class DebugItem(
    val text: String,
    val testTag: String,
    val onClick: () -> Unit
)