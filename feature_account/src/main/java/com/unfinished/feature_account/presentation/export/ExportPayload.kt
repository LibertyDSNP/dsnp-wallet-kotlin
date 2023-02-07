package com.unfinished.feature_account.presentation.export

import android.os.Parcelable
import io.novafoundation.nova.runtime.multiNetwork.chain.model.ChainId
import kotlinx.android.parcel.Parcelize

@Parcelize
class ExportPayload(
    val metaId: Long,
    val chainId: ChainId
) : Parcelable
