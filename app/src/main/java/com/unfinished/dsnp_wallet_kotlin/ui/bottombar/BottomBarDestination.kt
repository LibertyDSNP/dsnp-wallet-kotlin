package com.unfinished.dsnp_wallet_kotlin.ui.bottombar

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.unfinished.dsnp_wallet_kotlin.ui.destinations.IdentityScreenDestination

enum class BottomBarDestination(
    val direction: DirectionDestinationSpec,
    @DrawableRes val icon: Int,
    @StringRes val label: Int
) {
    Home(
        direction = IdentityScreenDestination,
        icon = com.unfinished.uikit.R.drawable.home,
        label = com.unfinished.uikit.R.string.home
    )
}