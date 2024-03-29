package com.unfinished.account.presentation.export

import android.os.Parcelable
import com.unfinished.runtime.multiNetwork.chain.model.ChainId
import kotlinx.android.parcel.Parcelize

@Parcelize
class ExportPayload(
    val metaId: Long,
    val chainId: ChainId
) : Parcelable
