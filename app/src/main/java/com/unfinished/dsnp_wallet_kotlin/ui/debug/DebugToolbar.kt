package com.unfinished.dsnp_wallet_kotlin.ui.debug

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.unfinished.dsnp_wallet_kotlin.ui.destinations.DebugMenuDestination
import com.unfinished.dsnp_wallet_kotlin.util.Tag
import com.unfinished.uikit.MainTheme
import com.unfinished.uikit.components.PrimaryButton
import com.unfinished.uikit.exts.tag

@Composable
fun DebugToolbar(
    navController: NavHostController,
    hideDebugClick: () -> Unit
) {
    DebugToolbar(
        debugMenuClick = {
            navController.navigate(DebugMenuDestination.route)
        },
        hideDebugClick = hideDebugClick
    )
}

@Composable
fun DebugToolbar(
    debugMenuClick: () -> Unit,
    hideDebugClick: () -> Unit
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
            onClick = debugMenuClick,
            modifier = Modifier.tag(Tag.DebugToolbar.menu)
        )

        Spacer(modifier = Modifier.size(16.dp))
        PrimaryButton(
            text = "Hide debug",
            textPadding = 2.dp,
            onClick = hideDebugClick,
            modifier = Modifier.tag(Tag.DebugToolbar.hide)
        )
    }
}

@Preview
@Composable
fun SampleDebugToolbar() {
    MainTheme {
        DebugToolbar(
            debugMenuClick = {},
            hideDebugClick = {}
        )
    }
}

