package com.unfinished.dsnp_wallet_kotlin.ui.bottombar

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.unfinished.dsnp_wallet_kotlin.ui.destinations.IdentityScreenDestination
import com.unfinished.dsnp_wallet_kotlin.ui.destinations.SettingsScreenDestination

enum class BottomBarDestination(
    val direction: DirectionDestinationSpec,
    @DrawableRes val icon: Int,
    @StringRes val label: Int
) {
    Home(
        direction = IdentityScreenDestination,
        icon = com.unfinished.uikit.R.drawable.home,
        label = com.unfinished.uikit.R.string.home
    ),
    Settings(
        direction = SettingsScreenDestination,
        icon = com.unfinished.uikit.R.drawable.settings,
        label = com.unfinished.uikit.R.string.settings
    )
}