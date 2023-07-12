package com.unfinished.feature_account.presentation.model.account.details

import android.content.Context
import android.os.Bundle
import androidx.annotation.StringRes
import com.unfinished.feature_account.presentation.action.ExternalActions
import com.unfinished.common.R
import com.unfinished.data.util.castOrNull
import com.unfinished.common.view.bottomSheet.list.fixed.item
import com.unfinished.feature_account.presentation.action.CopyCallback
import com.unfinished.feature_account.presentation.action.ExternalActionsSheet
import com.unfinished.feature_account.presentation.action.ExternalViewCallback
import com.unfinished.runtime.multiNetwork.chain.model.Chain

class ChainAccountActionsSheet(
    context: Context,
    payload: ExternalActions.Payload,
    onCopy: CopyCallback,
    onViewExternal: ExternalViewCallback,
    private val availableAccountActions: Set<AccountAction>,
    private val onChange: (inChain: Chain) -> Unit,
    private val onExport: (inChain: Chain) -> Unit,
) : ExternalActionsSheet(context, payload, onCopy, onViewExternal) {

    enum class AccountAction {
        EXPORT,
        CHANGE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showAvailableAccountActions()
    }

    private fun showAvailableAccountActions() {
        availableAccountActions.forEach {
            when (it) {
                AccountAction.EXPORT -> maybeShowExport()
                AccountAction.CHANGE -> maybeShowChange()
            }
        }
    }

    private fun maybeShowExport() {
        accountAddress()?.let {
            item(R.drawable.ic_share_outline, R.string.account_export, showArrow = true) {
                onExport.invoke(payload.chain)
            }
        }
    }

    private fun maybeShowChange() {
        val address = accountAddress()

        if (address != null) {
            changeAccountItem(R.string.accounts_change_chain_secrets)
        } else {
            changeAccountItem(R.string.account_add_account)
        }
    }

    private fun changeAccountItem(@StringRes labelRes: Int) {
        item(R.drawable.ic_staking_operations, labelRes, showArrow = true) {
            onChange.invoke(payload.chain)
        }
    }

    private fun accountAddress() = payload.type.castOrNull<ExternalActions.Type.Address>()?.address
}
