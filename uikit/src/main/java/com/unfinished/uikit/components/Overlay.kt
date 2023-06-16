package com.unfinished.uikit.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import com.unfinished.uikit.MainColors
import com.unfinished.uikit.MainTheme

@Composable
fun Overlay(
    modifier: Modifier = Modifier
) {
    val gradient =
        Brush.verticalGradient(
            listOf(
                MainColors.transparentOverlay,
                MainColors.transparentOverlay.copy(alpha = 0f)
            )
        )

    Canvas(
        modifier = modifier,
        onDraw = {
            drawRect(gradient)
        }
    )
}

@Preview
@Composable
private fun SampleOverlay() {
    MainTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MainColors.background),
            verticalArrangement = Arrangement.Bottom
        ) {
            Overlay(
                modifier = Modifier
                    .fillMaxHeight(.5f)
                    .fillMaxWidth()
            )
        }
    }
}