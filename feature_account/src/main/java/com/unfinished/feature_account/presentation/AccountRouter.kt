package com.unfinished.feature_account.presentation

import com.unfinished.feature_account.presentation.export.ExportPayload
import com.unfinished.feature_account.presentation.export.json.confirm.ExportJsonConfirmPayload
import com.unfinished.feature_account.presentation.model.account.add.AddAccountPayload
import com.unfinished.feature_account.presentation.model.account.add.ImportAccountPayload
import io.novafoundation.nova.common.navigation.ReturnableRouter
import com.unfinished.feature_account.presentation.mnemonic.confirm.ConfirmMnemonicPayload
import io.novafoundation.nova.common.navigation.DelayedNavigation
import io.novafoundation.nova.common.navigation.PinRequired
import io.novafoundation.nova.common.navigation.SecureRouter

interface AccountRouter : SecureRouter, ReturnableRouter {

    fun openMain()

    fun openCreatePincode()

    fun openConfirmHandleScreen(handle: String?)
    fun openTermsHandleScreen()
    fun openTabScreen(skip: Boolean, identitySuccess: Boolean)

    fun openMnemonicScreen(accountName: String?, addAccountPayload: AddAccountPayload)

    fun openConfirmMnemonicOnCreate(confirmMnemonicPayload: ConfirmMnemonicPayload)

    fun openAddAccount(payload: AddAccountPayload)

    fun openAccountDetails(metaAccountId: Long)

    @PinRequired
    fun exportMnemonicAction(exportPayload: ExportPayload): DelayedNavigation

    @PinRequired
    fun exportSeedAction(exportPayload: ExportPayload): DelayedNavigation

    @PinRequired
    fun exportJsonPasswordAction(exportPayload: ExportPayload): DelayedNavigation

    fun openExportJsonConfirm(payload: ExportJsonConfirmPayload)

    fun openImportAccountScreen(payload: ImportAccountPayload)

    fun finishExportFlow()

}
