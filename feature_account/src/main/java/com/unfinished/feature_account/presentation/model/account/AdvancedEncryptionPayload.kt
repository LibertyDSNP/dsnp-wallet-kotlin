package com.unfinished.feature_account.presentation.model.account

import android.os.Parcelable
import com.unfinished.feature_account.presentation.model.account.add.AddAccountPayload
import com.unfinished.runtime.multiNetwork.chain.model.ChainId
import kotlinx.android.parcel.Parcelize

sealed class AdvancedEncryptionPayload : Parcelable {

    @Parcelize
    class Change(val addAccountPayload: AddAccountPayload) : AdvancedEncryptionPayload()

    @Parcelize
    class View(
        val metaAccountId: Long,
        val chainId: ChainId,
        val hideDerivationPaths: Boolean = false
    ) : AdvancedEncryptionPayload()
}
