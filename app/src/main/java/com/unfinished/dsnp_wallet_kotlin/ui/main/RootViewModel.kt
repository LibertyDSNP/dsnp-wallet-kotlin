package com.unfinished.dsnp_wallet_kotlin.ui.main

import dagger.hilt.android.lifecycle.HiltViewModel
import io.novafoundation.nova.common.R
import io.novafoundation.nova.common.base.BaseViewModel
import io.novafoundation.nova.common.mixin.api.NetworkStateMixin
import io.novafoundation.nova.common.mixin.api.NetworkStateUi
import io.novafoundation.nova.common.resources.ResourceManager
import io.novafoundation.nova.runtime.multiNetwork.connection.ChainConnection
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class RootViewModel @Inject constructor(
    private val rootRouter: RootRouter,
    private val externalConnectionRequirementFlow: MutableStateFlow<ChainConnection.ExternalRequirement>,
    private val resourceManager: ResourceManager,
    private val networkStateMixin: NetworkStateMixin,
) : BaseViewModel(), NetworkStateUi by networkStateMixin {

    private var willBeClearedForLanguageChange = false

    override fun onCleared() {
        super.onCleared()

        externalConnectionRequirementFlow.value = ChainConnection.ExternalRequirement.FORBIDDEN
    }

    fun noticeInBackground() {
        if (!willBeClearedForLanguageChange) {
            externalConnectionRequirementFlow.value = ChainConnection.ExternalRequirement.STOPPED
        }
    }

    fun noticeInForeground() {
        if (externalConnectionRequirementFlow.value == ChainConnection.ExternalRequirement.STOPPED) {
            externalConnectionRequirementFlow.value = ChainConnection.ExternalRequirement.ALLOWED
        }
    }

    fun noticeLanguageLanguage() {
        willBeClearedForLanguageChange = true
    }

    fun restoredAfterConfigChange() {
        if (willBeClearedForLanguageChange) {
            rootRouter.returnToWallet()
            willBeClearedForLanguageChange = false
        }
    }

    fun externalUrlOpened(uri: String) {
        if (isBuyProviderRedirectLink(uri)) {
            showMessage(resourceManager.getString(R.string.buy_completed))
        }
    }

    fun isBuyProviderRedirectLink(link: String) = RootActivity.REDIRECT_URL_BASE in link

}
