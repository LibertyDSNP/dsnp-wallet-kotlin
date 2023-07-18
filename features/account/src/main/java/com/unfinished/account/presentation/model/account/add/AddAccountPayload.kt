package com.unfinished.account.presentation.model.account.add

import android.os.Parcelable
import com.unfinished.data.multiNetwork.chain.model.ChainId
import kotlinx.android.parcel.Parcelize

sealed class AddAccountPayload : Parcelable {

    @Parcelize
    class MetaAccount(val accountName: String) : AddAccountPayload()

    @Parcelize
    class ChainAccount(val chainId: ChainId, val metaId: Long) : AddAccountPayload()
}

val AddAccountPayload.chainIdOrNull
    get() = (this as? AddAccountPayload.ChainAccount)?.chainId
