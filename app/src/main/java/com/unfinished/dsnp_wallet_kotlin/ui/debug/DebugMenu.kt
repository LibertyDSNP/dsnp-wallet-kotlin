package com.unfinished.dsnp_wallet_kotlin.ui.debug

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.unfinished.dsnp_wallet_kotlin.ui.DebugNavGraph
import com.unfinished.dsnp_wallet_kotlin.ui.destinations.DebugNavigateScreenDestination
import com.unfinished.dsnp_wallet_kotlin.ui.destinations.TestScreenDestination
import com.unfinished.uikit.MainColors
import com.unfinished.uikit.MainTheme
import com.unfinished.uikit.components.Back
import com.unfinished.uikit.components.SimpleToolbar

private class DebugItem(
    val text: String,
    val onClick: () -> Unit
)

@DebugNavGraph(start = true)
@Destination
@Composable
fun DebugMenu(
    navigator: DestinationsNavigator
) {
    DebugMenu(
        debugItems = listOf(
            DebugItem(
                text = "Navigate",
                onClick = { navigator.navigate(DebugNavigateScreenDestination) }
            ),
            DebugItem(
                text = "Frequency Chain Test",
                onClick = { navigator.navigate(TestScreenDestination) }
            )
        )
    )
}

@Composable
private fun DebugMenu(
    debugItems: List<DebugItem>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MainColors.background)
    ) {
        SimpleToolbar(title = "Debug Menu")

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(debugItems) { index, debugItem ->
                DebugMenuItem(
                    text = debugItem.text,
                    onClick = debugItem.onClick
                )

                if (index != debugItems.lastIndex) Divider(
                    color = MainColors.divider
                )
            }
        }
    }
}

@Composable
fun DebugMenuItem(
    text: String,
    onClick: () -> Unit
) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable(onClick = onClick),
        fontSize = 24.sp,
        color = MainColors.onBackground
    )
}

@Preview
@Composable
fun SampleDebugMenu() {
    MainTheme {
        SampleDebugMenu()
    }
}