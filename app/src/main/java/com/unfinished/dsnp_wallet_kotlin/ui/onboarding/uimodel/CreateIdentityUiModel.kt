package com.unfinished.dsnp_wallet_kotlin.ui.onboarding.uimodel

data class CreateIdentityUiModel(
    val handle: String = "",
    val handleIsValid: Boolean = false,
    val suffix: String = "##",
    val currentStep: Int = 1,
    val totalSteps: Int = 3,
    val restoreWalletUiModel: RestoreWalletUiModel = RestoreWalletUiModel(),
    val showLoading: Boolean = false
)