package com.unfinished.feature_account.presentation.model.account.add

import android.os.Parcelable
import io.novafoundation.nova.runtime.multiNetwork.chain.model.ChainId
import kotlinx.android.parcel.Parcelize

sealed class AddAccountPayload : Parcelable {

    @Parcelize
    object MetaAccount : AddAccountPayload()

    @Parcelize
    class ChainAccount(val chainId: ChainId, val metaId: Long) : AddAccountPayload()
}

val AddAccountPayload.chainIdOrNull
    get() = (this as? AddAccountPayload.ChainAccount)?.chainId
