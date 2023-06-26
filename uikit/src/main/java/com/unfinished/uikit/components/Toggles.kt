package com.unfinished.uikit.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.unfinished.uikit.MainColors
import com.unfinished.uikit.MainTheme

@Composable
fun PrimaryToggle(
    isChecked: Boolean,
    isEnabled: Boolean = true,
    checkChanged: ((Boolean) -> Unit)? = null
) {
    Switch(
        checked = isChecked,
        onCheckedChange = checkChanged,
        enabled = isEnabled,
        colors = SwitchDefaults.colors(
            checkedThumbColor = MainColors.toggleOn,
            checkedTrackColor = MainColors.toggleOnTrack,
            uncheckedThumbColor = MainColors.toggleOff,
            uncheckedTrackColor = MainColors.toggleOffTrack,
            uncheckedTrackAlpha = 1F
        )
    )
}

@Preview
@Composable
private fun SampleToggles() {
    MainTheme {
        Column {
            PrimaryToggle(
                isChecked = false
            )
            PrimaryToggle(
                isChecked = true
            )
        }
    }
}