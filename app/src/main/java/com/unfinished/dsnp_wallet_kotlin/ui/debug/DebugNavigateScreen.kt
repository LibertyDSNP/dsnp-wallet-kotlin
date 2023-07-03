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
import com.unfinished.dsnp_wallet_kotlin.util.Tag
import com.unfinished.dsnp_wallet_kotlin.util.exts.navigateWithNoBackstack
import com.unfinished.uikit.MainColors
import com.unfinished.uikit.MainTheme
import com.unfinished.uikit.components.Back
import com.unfinished.uikit.components.SimpleToolbar

@DebugNavGraph
@Destination
@Composable
fun DebugNavigateScreen(
    navigator: DestinationsNavigator,
) {
    DebugNavigateScreen(
        debugItems = listOf(
            DebugItem(
                text = "Splash Screen",
                testTag = Tag.DebugNavigateScreen.splash,
                onClick = {
                    navigator.navigateWithNoBackstack(NavGraphs.splash)
                }
            ),
            DebugItem(
                text = "Landing Screen",
                testTag = Tag.DebugNavigateScreen.landing,
                onClick = {
                    navigator.navigateWithNoBackstack(NavGraphs.landing)
                }
            ),
            DebugItem(
                text = "Home Screen",
                testTag = Tag.DebugNavigateScreen.home,
                onClick = {
                    navigator.navigateWithNoBackstack(NavGraphs.main)
                }
            )
        )
    )
}

@Composable
private fun DebugNavigateScreen(
    debugItems: List<DebugItem>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MainColors.background)
    ) {
        SimpleToolbar(
            title = "Debug Navigate",
            testTag = Tag.DebugNavigateScreen.title
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(debugItems) { index, debugItem ->
                DebugMenuItem(
                    debugItem = debugItem
                )

                if (index != debugItems.lastIndex) Divider(
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
            debugItems = listOf(
                DebugItem(
                    text = "Sample Text",
                    onClick = {},
                    testTag = ""
                )
            )
        )
    }
}