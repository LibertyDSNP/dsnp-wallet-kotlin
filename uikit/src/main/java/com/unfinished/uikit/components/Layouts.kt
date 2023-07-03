package com.unfinished.uikit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unfinished.uikit.MainColors
import com.unfinished.uikit.MainTheme

@Composable
fun LogoLayout(
    modifier: Modifier = Modifier,
    logoTestTag: String,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MainColors.background)
            .then(modifier),
        horizontalAlignment = horizontalAlignment
    ) {
        Spacer(modifier = Modifier.size(46.dp))
        Logo(modifier = Modifier.testTag(logoTestTag))
        content()
    }
}

@Preview
@Composable
fun SampleLogoLayout() {
    MainTheme {
        LogoLayout(
            logoTestTag = "",
            content = {}
        )
    }
}