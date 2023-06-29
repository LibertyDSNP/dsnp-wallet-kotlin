package com.unfinished.uikit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.unfinished.uikit.MainColors
import com.unfinished.uikit.MainShapes
import com.unfinished.uikit.MainTheme

@Composable
fun CloseableDialog(
    content: @Composable () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        ),
        content = {
            Column(
                modifier = Modifier
                    .clip(MainShapes.dialog)
                    .background(MainColors.dialog)
                    .padding(16.dp)
            ) {
                DialogClose(
                    modifier = Modifier
                        .align(Alignment.End),
                    tint = MainColors.onDialog,
                    onClick = onDismiss,
                )
                Spacer(modifier = Modifier.size(4.dp))
                content()
            }
        }
    )
}

@Preview
@Composable
fun SampleCloseableDialog() {
    MainTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MainColors.background)
        ) {
            CloseableDialog(
                onDismiss = {},
                content = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Sample")
                    }
                }
            )
        }
    }
}