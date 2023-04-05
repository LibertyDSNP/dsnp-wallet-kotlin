package com.unfinished.feature_account.presentation.model.account.add

import android.os.Parcelable
import com.unfinished.feature_account.presentation.model.account.add.AddAccountPayload
import kotlinx.android.parcel.Parcelize

@Parcelize
class ImportAccountPayload(
    val type: SecretType,
    val addAccountPayload: AddAccountPayload,
) : Parcelable
