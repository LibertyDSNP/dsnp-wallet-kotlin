package com.unfinished.feature_account.presentation.action

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import com.unfinished.feature_account.R
import com.unfinished.feature_account.databinding.BottomSheetExternalActionsBinding
import io.novafoundation.nova.common.utils.makeGone
import io.novafoundation.nova.common.utils.makeVisible
import io.novafoundation.nova.common.view.bottomSheet.list.fixed.FixedListBottomSheet
import io.novafoundation.nova.common.view.bottomSheet.list.fixed.item
import io.novafoundation.nova.runtime.ext.availableExplorersFor
import io.novafoundation.nova.runtime.multiNetwork.chain.model.Chain

typealias ExternalViewCallback = (Chain.Explorer, ExternalActions.Type) -> Unit
typealias CopyCallback = (String) -> Unit

open class ExternalActionsSheet(
    context: Context,
    protected val payload: ExternalActions.Payload,
    val onCopy: CopyCallback,
    val onViewExternal: ExternalViewCallback,
) : FixedListBottomSheet(
    context,
//    viewConfiguration = ViewConfiguration(
//        layout = R.layout.bottom_sheet_external_actions,
//        title = {   },
//        container = {   }
//    )
) {

    lateinit var binding: BottomSheetExternalActionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = BottomSheetExternalActionsBinding.inflate(layoutInflater)

        if (payload.chainUi != null) {
            binding.externalActionsChain.makeVisible()
            binding.externalActionsChain.setChain(payload.chainUi)
        } else {
            binding.externalActionsChain.makeGone()
        }

        if (payload.icon != null) {
            binding.externalActionsIcon.makeVisible()
            binding.externalActionsIcon.setImageDrawable(payload.icon)
        } else {
            binding.externalActionsIcon.makeGone()
        }

        val primaryValue = payload.type.primaryValue

        setTitle(primaryValue)

        primaryValue?.let {
            item(io.novafoundation.nova.common.R.drawable.ic_copy_outline, payload.copyLabelRes) {
                onCopy(primaryValue)
            }

            showExplorers()
        }
    }

    private fun showExplorers() {
        payload.chain
            .availableExplorersFor(payload.type.explorerTemplateExtractor)
            .forEach { explorer ->
                val title = context.getString(io.novafoundation.nova.common.R.string.transaction_details_view_explorer, explorer.name)

                item(io.novafoundation.nova.common.R.drawable.ic_browser_outline, title, showArrow = true) {
                    onViewExternal(explorer, payload.type)
                }
            }
    }
}
