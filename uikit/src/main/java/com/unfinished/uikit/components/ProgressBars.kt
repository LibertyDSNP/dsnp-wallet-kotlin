package com.unfinished.uikit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unfinished.uikit.MainColors
import com.unfinished.uikit.MainTheme

@Composable
fun RoundedProgressBar(
    modifier: Modifier = Modifier,
    progress: Float,
) {
    val roundedCorners = RoundedCornerShape(10.dp)
    Box(
        modifier = Modifier.fillMaxWidth().then(modifier)
            .clip(roundedCorners)
            .background(MainColors.progressBarBackground)
            .height(12.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(roundedCorners)
                .background(MainColors.progressBar)
                .fillMaxHeight()
                .fillMaxWidth(progress)
        )
    }
}


@Preview
@Composable
private fun SampleProgressBars() {
    MainTheme {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(MainColors.background)
            .padding(24.dp)
        ) {
            RoundedProgressBar(progress = .5f)
        }
    }
}