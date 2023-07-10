package com.unfinished.feature_account.presentation.pincode

import android.os.Parcelable
import androidx.annotation.StringRes
import com.unfinished.feature_account.R
import com.unfinished.common.R as commonR
import com.unfinished.common.navigation.DelayedNavigation
import kotlinx.android.parcel.Parcelize

@Parcelize
class ToolbarConfiguration(@StringRes val titleRes: Int? = null, val backVisible: Boolean = false) : Parcelable

sealed class PinCodeAction(open val toolbarConfiguration: ToolbarConfiguration) : Parcelable {

    @Parcelize class Create(val delayedNavigation: DelayedNavigation) :
        PinCodeAction(ToolbarConfiguration(commonR.string.pincode_title_create, false))

    @Parcelize class Check(
        val delayedNavigation: DelayedNavigation,
        override val toolbarConfiguration: ToolbarConfiguration
    ) : PinCodeAction(toolbarConfiguration)

    @Parcelize object Change :
        PinCodeAction(ToolbarConfiguration(commonR.string.profile_pincode_change_title, true))
}
