package com.unfinished.dsnp_wallet_kotlin.ui.splash

import io.novafoundation.nova.common.navigation.SecureRouter

interface SplashRouter : SecureRouter {

    fun openAddFirstAccount()

    fun openCreatePincode()

    fun openInitialCheckPincode()
}
