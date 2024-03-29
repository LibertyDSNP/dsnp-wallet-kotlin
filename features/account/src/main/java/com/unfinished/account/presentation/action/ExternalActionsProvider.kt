package com.unfinished.account.presentation.action

import androidx.lifecycle.MutableLiveData
import com.unfinished.account.data.mappers.mapChainToUi
import com.unfinished.account.presentation.icon.createAccountAddressModel
import com.unfinished.common.R
import com.unfinished.common.address.AddressIconGenerator
import com.unfinished.common.resources.ClipboardManager
import com.unfinished.common.resources.ResourceManager
import com.unfinished.common.utils.Event
import com.unfinished.runtime.util.accountUrlOf
import com.unfinished.runtime.util.eventUrlOf
import com.unfinished.runtime.util.extrinsicUrlOf
import com.unfinished.runtime.multiNetwork.chain.model.Chain

class ExternalActionsProvider(
    private val clipboardManager: ClipboardManager,
    private val resourceManager: ResourceManager,
    private val addressIconGenerator: AddressIconGenerator,
) : ExternalActions.Presentation {

    override val openBrowserEvent = MutableLiveData<Event<String>>()

    override val showExternalActionsEvent = MutableLiveData<Event<ExternalActions.Payload>>()

    override fun viewExternalClicked(explorer: Chain.Explorer, type: ExternalActions.Type) {
        val url = when (type) {
            is ExternalActions.Type.Address -> type.address?.let { explorer.accountUrlOf(it) }
            is ExternalActions.Type.Event -> explorer.eventUrlOf(type.id)
            is ExternalActions.Type.Extrinsic -> explorer.extrinsicUrlOf(type.hash)
        }

        url?.let { showBrowser(url) }
    }

    override fun showBrowser(url: String) {
        openBrowserEvent.value = Event(url)
    }

    override suspend fun showExternalActions(type: ExternalActions.Type, chain: Chain) {
        val copyLabelRes = when (type) {
            is ExternalActions.Type.Address -> R.string.common_copy_address
            is ExternalActions.Type.Event -> R.string.common_copy_id
            is ExternalActions.Type.Extrinsic -> R.string.transaction_details_copy_hash
        }

        // only show chain button for address as for now
        val chainUi = when (type) {
            is ExternalActions.Type.Address -> mapChainToUi(chain)
            else -> null
        }

        // only show icon for address as for now
        val icon = when (type) {
            is ExternalActions.Type.Address -> if (type.address != null) {
                addressIconGenerator.createAccountAddressModel(chain, type.address, name = null).image
            } else {
                resourceManager.getDrawable(R.drawable.ic_identicon_placeholder)
            }
            else -> null
        }

        val payload = ExternalActions.Payload(
            type = type,
            chain = chain,
            chainUi = chainUi,
            icon = icon,
            copyLabelRes = copyLabelRes
        )

        showExternalActionsEvent.value = Event(payload)
    }

    override fun copyAddress(address: String, messageShower: (message: String) -> Unit) {
        clipboardManager.addToClipboard(address)

        val message = resourceManager.getString(R.string.common_copied)

        messageShower.invoke(message)
    }
}
