package com.unfinished.dsnp_wallet_kotlin.ui.tabs.settings

import com.unfinished.feature_account.presentation.AccountRouter
import com.unfinished.feature_account.presentation.model.account.add.AddAccountPayload
import dagger.hilt.android.lifecycle.HiltViewModel
import io.novafoundation.nova.common.base.BaseViewModel
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val router: AccountRouter,
) : BaseViewModel() {

    fun openMnemonicScreen(){
        router.openMnemonicScreen("test${Random.nextInt(1,500)}",AddAccountPayload.MetaAccount)
    }

}
