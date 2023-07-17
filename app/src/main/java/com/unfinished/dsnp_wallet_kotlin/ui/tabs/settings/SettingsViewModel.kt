package com.unfinished.dsnp_wallet_kotlin.ui.tabs.settings

import com.unfinished.account.presentation.AccountRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import com.unfinished.common.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val router: AccountRouter,
) : BaseViewModel() {

    fun openMnemonicScreen(){
        //router.openMnemonicScreen("test${Random.nextInt(1,500)}",AddAccountPayload.MetaAccount)
    }

}
