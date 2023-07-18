package com.unfinished.account.presentation.model.account

import android.os.Parcelable
import com.unfinished.account.presentation.model.account.add.AddAccountPayload
import com.unfinished.data.multiNetwork.chain.model.ChainId
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
