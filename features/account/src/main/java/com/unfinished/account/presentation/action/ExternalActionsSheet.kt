package com.unfinished.account.presentation.action

import android.content.Context
import android.os.Bundle
import com.unfinished.account.databinding.BottomSheetExternalActionsBinding
import com.unfinished.common.utils.makeGone
import com.unfinished.common.utils.makeVisible
import com.unfinished.common.view.bottomSheet.list.fixed.FixedListBottomSheet
import com.unfinished.common.view.bottomSheet.list.fixed.item
import com.unfinished.runtime.util.availableExplorersFor
import com.unfinished.runtime.multiNetwork.chain.model.Chain

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
            item(com.unfinished.common.R.drawable.ic_copy_outline, payload.copyLabelRes) {
                onCopy(primaryValue)
            }

            showExplorers()
        }
    }

    private fun showExplorers() {
        payload.chain
            .availableExplorersFor(payload.type.explorerTemplateExtractor)
            .forEach { explorer ->
                val title = context.getString(com.unfinished.common.R.string.transaction_details_view_explorer, explorer.name)

                item(com.unfinished.common.R.drawable.ic_browser_outline, title, showArrow = true) {
                    onViewExternal(explorer, payload.type)
                }
            }
    }
}
