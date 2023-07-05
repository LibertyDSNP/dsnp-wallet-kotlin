package com.unfinished.uikit.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.unfinished.uikit.MainColors
import com.unfinished.uikit.MainShapes
import com.unfinished.uikit.MainTheme
import com.unfinished.uikit.MainTypography

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String,
    textPadding: Dp = 6.dp,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MainColors.button,
            disabledContainerColor = MainColors.buttonDisabled
        ), modifier = modifier,
        enabled = enabled,
        shape = MainShapes.button,
        content = {
            Text(
                text = text,
                style = MainTypography.bodyMedium,
                color = if (enabled) MainColors.onButton else MainColors.onButtonDisabled,
                modifier = Modifier.padding(vertical = textPadding)
            )
        })
}

@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String,
    textPadding: Dp = 6.dp,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = MainColors.buttonDisabled
        ), modifier = modifier.border(
            width = 1.dp,
            shape = MainShapes.button,
            color = MainColors.button
        ),
        enabled = enabled,
        shape = MainShapes.button,
        content = {
            Text(
                text = text,
                style = MainTypography.bodyMedium,
                color = if (enabled) MainColors.onButton else MainColors.onButtonDisabled,
                modifier = Modifier.padding(vertical = textPadding),
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Visible
            )
        })
}

@Preview
@Composable
private fun SampleButtons() {
    MainTheme {
        Column {
            PrimaryButton(text = "Primary Button", onClick = {})

            Spacer(modifier = Modifier.size(24.dp))

            SecondaryButton(text = "Secondary Button", onClick = {})
        }
    }
}