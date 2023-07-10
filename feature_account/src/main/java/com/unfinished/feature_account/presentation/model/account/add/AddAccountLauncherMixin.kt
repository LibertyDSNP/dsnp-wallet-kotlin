package com.unfinished.feature_account.presentation.model.account.add

import androidx.lifecycle.LiveData
import com.unfinished.feature_account.domain.model.MetaAccount
import com.unfinished.feature_account.presentation.mixin.importType.ImportTypeChooserMixin
import com.unfinished.common.utils.Event
import com.unfinished.runtime.multiNetwork.chain.model.Chain

interface AddAccountLauncherMixin {

    class AddAccountTypePayload(
        val title: String,
        val onCreate: () -> Unit,
        val onImport: () -> Unit
    )

    val showAddAccountTypeChooser: LiveData<Event<AddAccountTypePayload>>

    val showImportTypeChooser: LiveData<Event<ImportTypeChooserMixin.Payload>>

    interface Presentation : AddAccountLauncherMixin {

        enum class Mode {
            ADD, CHANGE
        }

        fun initiateLaunch(
            chain: Chain,
            metaAccount: MetaAccount,
        )
    }
}
