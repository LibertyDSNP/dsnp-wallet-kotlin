package com.unfinished.dsnp_wallet_kotlin.root

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.unfinished.dsnp_wallet_kotlin.R
import com.unfinished.dsnp_wallet_kotlin.ui.onboarding.OnboardingRouter
import com.unfinished.dsnp_wallet_kotlin.ui.main.RootRouter
import com.unfinished.dsnp_wallet_kotlin.ui.onboarding.LandingFragment
import com.unfinished.dsnp_wallet_kotlin.ui.splash.SplashRouter
import com.unfinished.feature_account.presentation.AccountRouter
import com.unfinished.feature_account.presentation.export.ExportPayload
import com.unfinished.feature_account.presentation.export.json.confirm.ExportJsonConfirmFragment
import com.unfinished.feature_account.presentation.export.json.confirm.ExportJsonConfirmPayload
import com.unfinished.feature_account.presentation.export.json.password.ExportJsonPasswordFragment
import com.unfinished.feature_account.presentation.export.seed.ExportSeedFragment
import com.unfinished.feature_account.presentation.mnemonic.backup.BackupMnemonicFragment
import com.unfinished.feature_account.presentation.mnemonic.backup.BackupMnemonicPayload
import com.unfinished.feature_account.presentation.mnemonic.confirm.ConfirmMnemonicFragment
import com.unfinished.feature_account.presentation.model.account.add.AddAccountPayload
import com.unfinished.feature_account.presentation.model.account.add.ImportAccountPayload
import com.unfinished.feature_account.presentation.mnemonic.confirm.ConfirmMnemonicPayload
import com.unfinished.feature_account.presentation.pincode.PinCodeAction
import com.unfinished.feature_account.presentation.pincode.PincodeFragment
import com.unfinished.feature_account.presentation.pincode.ToolbarConfiguration
import io.novafoundation.nova.common.navigation.DelayedNavigation
import io.novafoundation.nova.common.utils.postToUiThread
import kotlinx.parcelize.Parcelize


@Parcelize
class NavComponentDelayedNavigation(val globalActionId: Int, val extras: Bundle? = null) :
    DelayedNavigation

class Navigator(
    private val navigationHolder: NavigationHolder,
) : AccountRouter, OnboardingRouter, RootRouter, SplashRouter {

    private val navController: NavController?
        get() = navigationHolder.navController

    override fun openMain() {
        navController?.navigate(R.id.action_open_main)
    }

    override fun returnToWallet() {
        // to achieve smooth animation
        postToUiThread {
            navController?.navigate(R.id.action_return_to_wallet)
        }
    }

    override fun openAddFirstAccount() {
        navController?.navigate(R.id.action_splashFragment_to_landingFragment, LandingFragment.bundle(true))
    }


    override fun openInitialCheckPincode() {
        val action = PinCodeAction.Check(NavComponentDelayedNavigation(R.id.action_open_main), ToolbarConfiguration())
        val bundle = PincodeFragment.getPinCodeBundle(action)
        navController?.navigate(R.id.action_splashFragment_to_pincodeFragment, bundle)
    }

    override fun openCreatePincode() {
        val bundle = buildCreatePinBundle()
        when (navController?.currentDestination?.id) {
            R.id.splashFragment -> navController?.navigate(R.id.action_splashFragment_to_pincodeFragment, bundle)
            R.id.confirmMnemonicFragment -> navController?.navigate(R.id.action_confirmMnemonicFragment_to_pincodeFragment, bundle)
        }
    }

    override fun openAfterPinCode(delayedNavigation: DelayedNavigation) {
        require(delayedNavigation is NavComponentDelayedNavigation)
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.pincodeFragment, true)
//            .setEnterAnim(androidx.transition.R.anim.fragment_open_enter)
//            .setExitAnim(androidx.transition.R.anim.fragment_open_exit)
//            .setPopEnterAnim(androidx.transition.R.anim.fragment_close_enter)
//            .setPopExitAnim(androidx.transition.R.anim.fragment_close_exit)
            .build()

        navController?.navigate(delayedNavigation.globalActionId, delayedNavigation.extras, navOptions)
    }

    override fun back() {
        navigationHolder.executeBack()
    }

    override fun openConfirmMnemonicOnCreate(confirmMnemonicPayload: ConfirmMnemonicPayload) {
        val bundle = ConfirmMnemonicFragment.getBundle(confirmMnemonicPayload)

        navController?.navigate(
            R.id.action_backupMnemonicFragment_to_confirmMnemonicFragment,
            bundle
        )
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

    override fun openCreateAccount(accountName: String?, addAccountPayload: AddAccountPayload) {
        val payload = BackupMnemonicPayload.Create(accountName, addAccountPayload)
        navController?.navigate(R.id.action_lookupFragment_to_mnemonic_nav_graph, BackupMnemonicFragment.getBundle(payload))
    }

    override fun openLookupScreen() {
         navController?.navigate(R.id.action_landingFragment_to_lookupFragment)
    }

    override fun openMnemonicScreen(accountName: String?, addAccountPayload: AddAccountPayload) {
        val destination = when (val currentDestinationId = navController?.currentDestination?.id) {
            R.id.lookupFragment -> R.id.action_lookupFragment_to_mnemonic_nav_graph
            else -> throw IllegalArgumentException("Unknown current destination to open mnemonic screen: $currentDestinationId")
        }
        val payload = BackupMnemonicPayload.Create(accountName, addAccountPayload)
        navController?.navigate(destination, BackupMnemonicFragment.getBundle(payload))
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
        return NavComponentDelayedNavigation(com.google.android.flexbox.R.id.wrap, Bundle())
    }

    override fun exportSeedAction(exportPayload: ExportPayload): DelayedNavigation {
        val extras = ExportSeedFragment.getBundle(exportPayload)
//
        return NavComponentDelayedNavigation(R.id.action_confirmMnemonicFragment_to_pincodeFragment, extras)
    }

    override fun exportJsonPasswordAction(exportPayload: ExportPayload): DelayedNavigation {
        val extras = ExportJsonPasswordFragment.getBundle(exportPayload)
//
        return NavComponentDelayedNavigation(R.id.action_backupMnemonicFragment_to_confirmMnemonicFragment, extras)
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
        val delayedNavigation = NavComponentDelayedNavigation(R.id.action_open_main)
        val action = PinCodeAction.Create(delayedNavigation)
        return PincodeFragment.getPinCodeBundle(action)
    }
}
