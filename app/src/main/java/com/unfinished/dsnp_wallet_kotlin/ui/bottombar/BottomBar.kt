package com.unfinished.dsnp_wallet_kotlin.ui.bottombar

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.unfinished.dsnp_wallet_kotlin.ui.NavGraphs
import com.unfinished.dsnp_wallet_kotlin.ui.appCurrentDestinationAsState
import com.unfinished.dsnp_wallet_kotlin.ui.destinations.Destination
import com.unfinished.dsnp_wallet_kotlin.ui.startAppDestination
import com.unfinished.uikit.MainColors
import com.unfinished.uikit.MainTheme
import com.unfinished.uikit.MainTypography

@Composable
fun BottomBar(
    navController: NavController
) {
    val currentDestination: Destination = navController.appCurrentDestinationAsState().value
        ?: NavGraphs.root.startAppDestination

    BottomNavigation(
        backgroundColor = MainColors.background
    ) {
        BottomBarDestination.values().forEach { destination ->
            val isSelected = destination.isSelected(currentDestination)
            val iconColor =
                if (isSelected) MainColors.bottomBarIcon else MainColors.bottomBarIconNotSelected
            BottomNavigationItem(
                selected = isSelected,
                onClick = {
                    if (!isSelected) navController.navigate(
                        route = destination.direction.route,
                        navOptions = navOptions {
                            launchSingleTop = true

                            popUpTo(
                                route = NavGraphs.root.route,
                                popUpToBuilder = {
                                    inclusive = true
                                }
                            )
                        }
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(id = destination.icon),
                        contentDescription = stringResource(destination.label),
                        tint = iconColor
                    )
                },
                label = {
                    Text(
                        text = stringResource(destination.label),
                        color = iconColor,
                        style = MainTypography.bottomBar
                    )
                },
            )
        }
    }
}

@Preview
@Composable
fun SampleBottomBar() {
    MainTheme {
        BottomBar(navController = rememberNavController())
    }
}