package com.unfinished.account.di

import android.os.Bundle
import androidx.navigation.NavController
import com.unfinished.account.presentation.AccountRouter
import com.unfinished.account.presentation.export.ExportPayload
import com.unfinished.account.presentation.export.json.confirm.ExportJsonConfirmFragment
import com.unfinished.account.presentation.export.json.confirm.ExportJsonConfirmPayload
import com.unfinished.account.presentation.export.json.password.ExportJsonPasswordFragment
import com.unfinished.account.presentation.export.seed.ExportSeedFragment
import com.unfinished.account.presentation.handle.confirm.ConfirmHandleFragment
import com.unfinished.account.presentation.handle.confirm.ConfirmHandlePayload
import com.unfinished.account.presentation.mnemonic.backup.BackupMnemonicPayload
import com.unfinished.account.presentation.mnemonic.confirm.ConfirmMnemonicFragment
import com.unfinished.account.presentation.mnemonic.confirm.ConfirmMnemonicPayload
import com.unfinished.account.presentation.model.account.add.AddAccountPayload
import com.unfinished.account.presentation.model.account.add.ImportAccountPayload
import com.unfinished.account.presentation.pincode.PinCodeAction
import com.unfinished.account.presentation.pincode.PincodeFragment
import com.unfinished.common.navigation.DelayedNavigation
import kotlinx.parcelize.Parcelize
import javax.inject.Inject


@Parcelize
class NavComponentDelayedNavigation(val globalActionId: Int, val extras: Bundle? = null) :
    DelayedNavigation

class Navigator @Inject constructor() : AccountRouter {

    private val navController: NavController?
        get() = null

    override fun openMain() {
        //navController?.navigate(R.id.action_open_main)
    }

    override fun openCreatePincode() {
        val bundle = buildCreatePinBundle()
//        when (navController?.currentDestination?.id) {
//            R.id.splashFragment -> navController?.navigate(R.id.action_splashFragment_to_pincodeFragment, bundle)
//            R.id.confirmMnemonicFragment -> navController?.navigate(R.id.action_confirmMnemonicFragment_to_pincodeFragment, bundle)
//        }
    }

    override fun openAfterPinCode(delayedNavigation: DelayedNavigation) {
        require(delayedNavigation is NavComponentDelayedNavigation)
//        val navOptions = NavOptions.Builder()
//            .setPopUpTo(R.id.pincodeFragment, true)
////            .setEnterAnim(androidx.transition.R.anim.fragment_open_enter)
////            .setExitAnim(androidx.transition.R.anim.fragment_open_exit)
////            .setPopEnterAnim(androidx.transition.R.anim.fragment_close_enter)
////            .setPopExitAnim(androidx.transition.R.anim.fragment_close_exit)
//            .build()
//
//        navController?.navigate(delayedNavigation.globalActionId, delayedNavigation.extras, navOptions)
    }

    override fun back() {
//        navigationHolder.executeBack()
    }

    override fun openConfirmMnemonicOnCreate(confirmMnemonicPayload: ConfirmMnemonicPayload) {
        val bundle = ConfirmMnemonicFragment.getBundle(confirmMnemonicPayload)

//        navController?.navigate(
//            R.id.action_backupMnemonicFragment_to_confirmMnemonicFragment,
//            bundle
//        )
    }

    override fun openImportAccountScreen(payload: ImportAccountPayload) {
//        val destination = when (val currentDestinationId = navController?.currentDestination?.id) {
//            R.id.welcomeFragment -> R.id.action_welcomeFragment_to_import_nav_graph
//            R.id.accountDetailsFragment -> R.id.action_accountDetailsFragment_to_import_nav_graph
//            else -> throw IllegalArgumentException("Unknown current destination to open import account screen: $currentDestinationId")
//        }
//
//        navController?.navigate(destination, ImportAccountFragment.getBundle(payload))
    }

    override fun finishExportFlow() {

    }

    override fun openMnemonicScreen(accountName: String?, addAccountPayload: AddAccountPayload) {

        val payload = BackupMnemonicPayload.Create(accountName, addAccountPayload)
        //navController?.navigate(R.id.action_tabFragment_to_Mnemonc, BackupMnemonicFragment.getBundle(payload))
    }

    override fun openAccountDetails(metaAccountId: Long) {
//        val extras = AccountDetailsFragment.getBundle(metaAccountId)
//
//        navController?.navigate(R.id.action_open_account_details, extras)
    }

    override fun openAddAccount(payload: AddAccountPayload) {
//        navController?.navigate(R.id.action_open_onboarding, WelcomeFragment.bundle(payload))
    }

    override fun exportMnemonicAction(exportPayload: ExportPayload): DelayedNavigation {
//        val payload = BackupMnemonicPayload.Confirm(exportPayload.chainId, exportPayload.metaId)
//        val extras = BackupMnemonicFragment.getBundle(payload)
//
        return NavComponentDelayedNavigation(0, Bundle())
    }

    override fun exportSeedAction(exportPayload: ExportPayload): DelayedNavigation {
        val extras = ExportSeedFragment.getBundle(exportPayload)
//
        return NavComponentDelayedNavigation(0, extras)
    }

    override fun exportJsonPasswordAction(exportPayload: ExportPayload): DelayedNavigation {
        val extras = ExportJsonPasswordFragment.getBundle(exportPayload)
//
        return NavComponentDelayedNavigation(0, extras)
    }

    override fun openExportJsonConfirm(payload: ExportJsonConfirmPayload) {
        val extras = ExportJsonConfirmFragment.getBundle(payload)

//        navController?.navigate(R.id.addressInputAddress, extras)
    }

    override fun withPinCodeCheckRequired(
        delayedNavigation: DelayedNavigation,
        createMode: Boolean,
        pinCodeTitleRes: Int?,
    ) {
//        val action = if (createMode) {
//            PinCodeAction.Create(delayedNavigation)
//        } else {
//            PinCodeAction.Check(delayedNavigation, ToolbarConfiguration(pinCodeTitleRes, true))
//        }
//
//        val extras = PincodeFragment.getPinCodeBundle(action)
//
//        navController?.navigate(R.id.open_pincode_check, extras)
    }

    private fun buildCreatePinBundle(): Bundle {
        val delayedNavigation = NavComponentDelayedNavigation(0)
        val action = PinCodeAction.Create(delayedNavigation)
        return PincodeFragment.getPinCodeBundle(action)
    }

    override fun openConfirmHandleScreen(handle: String?) {
        val payload = ConfirmHandlePayload(handle)
        navController?.navigate(0, ConfirmHandleFragment.getBundle(payload))
    }

    override fun openTermsHandleScreen() {
        navController?.navigate(0)
    }

    override fun openTabScreen(skip: Boolean, identitySuccess: Boolean) {
//        val destination = when (val currentDestinationId = navController?.currentDestination?.id) {
//            R.id.createHandleFragment -> R.id.action_createHandleFragment_to_tabFragment
//            R.id.termsHandleFragment -> R.id.action_termsHandleFragment_to_tabFragment
//            else -> throw IllegalArgumentException("Unknown current destination to open tab screen: $currentDestinationId")
//        }
//        navController?.navigate(destination,TabFragment.bundle(skip,identitySuccess))
    }
}
