package com.unfinished.feature_account.presentation.export.json.confirm

import android.os.Parcelable
import com.unfinished.feature_account.presentation.export.ExportPayload
import kotlinx.android.parcel.Parcelize

@Parcelize
class ExportJsonConfirmPayload(val exportPayload: ExportPayload, val json: String) : Parcelable
