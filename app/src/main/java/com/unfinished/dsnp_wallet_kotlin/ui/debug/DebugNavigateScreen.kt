package com.unfinished.dsnp_wallet_kotlin.ui.debug

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.unfinished.dsnp_wallet_kotlin.ui.DebugNavGraph
import com.unfinished.dsnp_wallet_kotlin.ui.NavGraph
import com.unfinished.dsnp_wallet_kotlin.ui.NavGraphs
import com.unfinished.uikit.MainColors
import com.unfinished.uikit.MainTheme
import com.unfinished.uikit.components.Back

private class NavigateItem(
    val text: String,
    val onClick: () -> Unit
)

@DebugNavGraph
@Destination
@Composable
fun DebugNavigateScreen(
    navigator: DestinationsNavigator,
) {
    DebugNavigateScreen(
        navigateItems = listOf(
            NavigateItem(
                text = "Splash Screen",
                onClick = {
                    navigator.navigateAwayFromDebug(NavGraphs.splash)
                }
            ),
            NavigateItem(
                text = "Landing Screen",
                onClick = {
                    navigator.navigateAwayFromDebug(NavGraphs.landing)
                }
            ),
            NavigateItem(
                text = "Home Screen",
                onClick = {
                    navigator.navigateAwayFromDebug(NavGraphs.main)
                }
            )
        )
    )
}

private fun DestinationsNavigator.navigateAwayFromDebug(
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

@Composable
private fun DebugNavigateScreen(
    navigateItems: List<NavigateItem>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MainColors.background)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Back()
            Text(
                text = "Debug Navigate",
                fontSize = 24.sp,
                color = MainColors.onBackground
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(navigateItems) { index, navigateItem ->
                DebugMenuItem(
                    text = navigateItem.text,
                    onClick = navigateItem.onClick
                )

                if (index != navigateItems.lastIndex) Divider(
                    color = MainColors.divider
                )

            }
        }
    }
}

@Preview
@Composable
fun SampleDebugNavigateScreen() {
    MainTheme {
        DebugNavigateScreen(
            navigateItems = listOf(
                NavigateItem(
                    text = "Sample Text",
                    onClick = {}
                )
            )
        )
    }
}