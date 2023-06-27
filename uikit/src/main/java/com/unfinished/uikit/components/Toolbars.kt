package com.unfinished.uikit.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unfinished.uikit.MainColors
import com.unfinished.uikit.MainTheme
import com.unfinished.uikit.MainTypography

@Composable
fun SimpleToolbar(
    title: String,
    showNav: Boolean = true
) {
    Box(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
    ) {
        if (showNav) Back(
            modifier = Modifier.align(Alignment.CenterStart)
        )

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = title,
            color = MainColors.onBackground,
            style = MainTypography.toolbarTitle,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun SampleToolbars() {
    MainTheme {
        Column {
            SimpleToolbar(title = "Sample")
        }
    }
}