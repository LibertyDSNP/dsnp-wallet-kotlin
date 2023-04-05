package com.unfinished.dsnp_wallet_kotlin.ui.onboarding

import com.unfinished.feature_account.presentation.model.account.add.AddAccountPayload
import com.unfinished.feature_account.presentation.model.account.add.ImportAccountPayload

interface OnboardingRouter {

    fun openLookupScreen()

    fun openCreateAccount(accountName: String?, addAccountPayload: AddAccountPayload)

    fun openMnemonicScreen(accountName: String?, addAccountPayload: AddAccountPayload)

    fun openImportAccountScreen(payload: ImportAccountPayload)

    fun back()
}
