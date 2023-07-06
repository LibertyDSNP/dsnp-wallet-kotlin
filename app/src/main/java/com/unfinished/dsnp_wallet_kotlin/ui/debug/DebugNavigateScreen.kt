package com.unfinished.dsnp_wallet_kotlin.ui.debug

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.unfinished.dsnp_wallet_kotlin.ui.DebugNavGraph
import com.unfinished.dsnp_wallet_kotlin.ui.NavGraphs
import com.unfinished.dsnp_wallet_kotlin.ui.destinations.RecoveryPhraseScreenDestination
import com.unfinished.dsnp_wallet_kotlin.ui.destinations.SettingsScreenDestination
import com.unfinished.dsnp_wallet_kotlin.util.Tag
import com.unfinished.dsnp_wallet_kotlin.util.exts.navigateWithHome
import com.unfinished.dsnp_wallet_kotlin.util.exts.navigateWithNoBackstack
import com.unfinished.uikit.MainColors
import com.unfinished.uikit.MainTheme
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
                text = "Landing",
                testTag = Tag.DebugNavigateScreen.landing,
                onClick = {
                    navigator.navigateWithNoBackstack(NavGraphs.landing)
                }
            ),
            DebugItem(
                text = "Home",
                testTag = Tag.DebugNavigateScreen.home,
                onClick = {
                    navigator.navigateWithNoBackstack(NavGraphs.main)
                }
            ),
            DebugItem(
                text = "Setting",
                testTag = Tag.DebugNavigateScreen.home,
                onClick = {
                    navigator.navigateWithHome(SettingsScreenDestination)
                }
            ),
            DebugItem(
                text = "Recovery Phrase",
                testTag = Tag.DebugNavigateScreen.home,
                onClick = {
                    navigator.navigateWithHome(RecoveryPhraseScreenDestination)
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