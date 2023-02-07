package com.unfinished.feature_account.presentation

import android.provider.Settings.Secure
import com.unfinished.feature_account.app.DelayedNavigation
import com.unfinished.feature_account.app.PinRequired
import com.unfinished.feature_account.app.SecureRouter
import com.unfinished.feature_account.presentation.export.ExportPayload
import com.unfinished.feature_account.presentation.export.json.confirm.ExportJsonConfirmPayload
import com.unfinished.feature_account.presentation.model.account.add.AddAccountPayload
import com.unfinished.feature_account.presentation.model.account.add.ImportAccountPayload
import com.unfinished.feature_account.presentation.model.mnemonic.ConfirmMnemonicPayload
import io.novafoundation.nova.common.navigation.ReturnableRouter

interface AccountRouter :SecureRouter, ReturnableRouter {

    fun openMain()

    fun openMnemonicScreen(accountName: String?, payload: AddAccountPayload)

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
