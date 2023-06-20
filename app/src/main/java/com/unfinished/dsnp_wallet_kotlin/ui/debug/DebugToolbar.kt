package com.unfinished.dsnp_wallet_kotlin.ui.debug

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.unfinished.dsnp_wallet_kotlin.ui.destinations.DebugMenuDestination
import com.unfinished.uikit.MainTheme
import com.unfinished.uikit.components.PrimaryButton

@Composable
fun DebugToolbar(
    navController: NavHostController
) {
    DebugToolbar(
        debugMenuClick = {
            navController.navigate(DebugMenuDestination.route)
        }
    )
}

@Composable
fun DebugToolbar(
    debugMenuClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PrimaryButton(
            text = "Debug menu",
            textPadding = 2.dp,
            onClick = debugMenuClick
        )
    }
}

@Preview
@Composable
fun SampleDebugToolbar() {
    MainTheme {
        DebugToolbar(
            debugMenuClick = {}
        )
    }
}

