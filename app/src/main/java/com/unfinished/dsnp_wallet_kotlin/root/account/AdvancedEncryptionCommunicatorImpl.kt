package com.unfinished.dsnp_wallet_kotlin.root.account

import com.unfinished.dsnp_wallet_kotlin.root.BaseInterScreenCommunicator
import com.unfinished.dsnp_wallet_kotlin.root.NavigationHolder
import com.unfinished.feature_account.presentation.AdvancedEncryptionCommunicator
import com.unfinished.feature_account.presentation.model.account.AdvancedEncryptionPayload

class AdvancedEncryptionCommunicatorImpl(
    navigationHolder: NavigationHolder
) : BaseInterScreenCommunicator<AdvancedEncryptionPayload, AdvancedEncryptionCommunicator.Response>(navigationHolder), AdvancedEncryptionCommunicator {

    override fun openRequest(request: AdvancedEncryptionPayload) {
       // navController.navigate(commonR.id.action_open_advancedEncryptionFragment, AdvancedEncryptionFragment.getBundle(request))
    }
}
