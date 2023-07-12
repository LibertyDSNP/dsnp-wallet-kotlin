package com.unfinished.account.presentation.model.account.add

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ImportAccountPayload(
    val type: SecretType,
    val addAccountPayload: AddAccountPayload,
) : Parcelable
