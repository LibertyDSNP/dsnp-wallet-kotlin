package com.unfinished.account.presentation.handle.confirm

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ConfirmHandlePayload(
    val handle: String?,
) : Parcelable
