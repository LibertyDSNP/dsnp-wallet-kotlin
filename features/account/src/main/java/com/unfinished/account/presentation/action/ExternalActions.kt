package com.unfinished.account.presentation.action

import android.graphics.drawable.Drawable
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import com.unfinished.account.presentation.model.chain.ChainUi
import com.unfinished.common.mixin.api.Browserable
import com.unfinished.common.utils.Event
import com.unfinished.runtime.util.addressOf
import com.unfinished.runtime.multiNetwork.chain.model.Chain
import com.unfinished.runtime.multiNetwork.chain.model.ExplorerTemplateExtractor
import jp.co.soramitsu.fearless_utils.runtime.AccountId

interface ExternalActions : Browserable {

    class Payload(
        val type: Type,
        val chain: Chain,
        val chainUi: ChainUi?,
        val icon: Drawable?,
        @StringRes val copyLabelRes: Int,
    )

    sealed class Type(
        val primaryValue: String?,
        val explorerTemplateExtractor: ExplorerTemplateExtractor,
    ) {

        class Address(val address: String?) : Type(address, explorerTemplateExtractor = Chain.Explorer::account)

        class Extrinsic(val hash: String) : Type(hash, explorerTemplateExtractor = Chain.Explorer::extrinsic)

        class Event(val id: String) : Type(id, explorerTemplateExtractor = Chain.Explorer::event)
    }

    val showExternalActionsEvent: LiveData<Event<Payload>>

    fun viewExternalClicked(explorer: Chain.Explorer, type: Type)

    fun copyAddress(address: String, messageShower: (message: String) -> Unit)

    interface Presentation : ExternalActions, Browserable.Presentation {

        suspend fun showExternalActions(type: Type, chain: Chain)
    }
}

suspend fun ExternalActions.Presentation.showAddressActions(accountId: AccountId, chain: Chain) = showExternalActions(
    type = ExternalActions.Type.Address(chain.addressOf(accountId)),
    chain = chain
)
