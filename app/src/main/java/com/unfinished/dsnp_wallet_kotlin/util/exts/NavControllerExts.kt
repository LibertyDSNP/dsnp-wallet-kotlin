package com.unfinished.dsnp_wallet_kotlin.util.exts

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.unfinished.dsnp_wallet_kotlin.ui.NavGraph
import com.unfinished.dsnp_wallet_kotlin.ui.NavGraphs

fun NavController.safeGetBackStackEntry(route: String): NavBackStackEntry {
    val hasRoute = backQueue.firstOrNull {
        it.destination.route == route
    } != null

    return if (hasRoute) {
        getBackStackEntry(route)
    } else {
        backQueue.first()
    }
}

fun DestinationsNavigator.navigateWithNoBackstack(
    navGraph: NavGraph
) {
    navigate(
        navGraph.route,
        builder = {
            launchSingleTop = true
            popUpTo(
                route = NavGraphs.root.route,
                popUpToBuilder = {
                    inclusive = true
                }
            )
        }
    )
}