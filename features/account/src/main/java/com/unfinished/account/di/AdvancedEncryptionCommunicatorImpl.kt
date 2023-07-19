package com.unfinished.account.di

import com.unfinished.account.presentation.AdvancedEncryptionCommunicator
import com.unfinished.account.presentation.model.account.AdvancedEncryptionPayload
import kotlinx.coroutines.flow.Flow

class AdvancedEncryptionCommunicatorImpl() : AdvancedEncryptionCommunicator {
    override val latestResponse: AdvancedEncryptionCommunicator.Response?
        get() = TODO("Not yet implemented")
    override val responseFlow: Flow<AdvancedEncryptionCommunicator.Response>
        get() = TODO("Not yet implemented")

    override fun openRequest(request: AdvancedEncryptionPayload) {
       // navController.navigate(commonR.id.action_open_advancedEncryptionFragment, AdvancedEncryptionFragment.getBundle(request))
    }

    override val lastState: AdvancedEncryptionCommunicator.Response?
        get() = TODO("Not yet implemented")

    override fun respond(response: AdvancedEncryptionCommunicator.Response) {
        TODO("Not yet implemented")
    }
}
