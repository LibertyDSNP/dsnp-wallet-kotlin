package com.unfinished.uikit.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unfinished.uikit.MainColors
import com.unfinished.uikit.MainTheme
import com.unfinished.uikit.MainTypography

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MainColors.button, disabledContainerColor = MainColors.buttonDisabled
        ), modifier = modifier,
        enabled = enabled,
        content = {
            Text(
                text = text,
                style = MainTypography.bodyMedium,
                color = MainColors.onButton,
                modifier = Modifier.padding(vertical = 6.dp)
            )
        })
}

@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MainColors.secondaryButton,
            disabledContainerColor = MainColors.secondaryButtonDisabled
        ),
        modifier = modifier,
        enabled = enabled,
        content = {
            Text(
                text = text,
                style = MainTypography.bodyMedium,
                color = MainColors.onButton,
                modifier = Modifier.padding(vertical = 6.dp)
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