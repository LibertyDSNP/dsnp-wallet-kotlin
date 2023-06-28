package com.unfinished.uikit.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unfinished.uikit.MainColors
import com.unfinished.uikit.MainShapes
import com.unfinished.uikit.MainTheme
import com.unfinished.uikit.MainTypography
import com.unfinished.uikit.R
import kotlinx.coroutines.launch

@Composable
fun SuccessSnackbar(
    modifier: Modifier = Modifier,
    text: String,
    showSnackbar: Boolean,
    onDismiss: () -> Unit = {},
    onShown: () -> Unit = {}
) {
    val snackState = remember { SnackbarHostState() }
    val snackScope = rememberCoroutineScope()

    SnackbarHost(
        modifier = modifier,
        hostState = snackState
    ) {
        Snackbar(
            action = {
                Close(
                    onClick = {
                        snackState.currentSnackbarData?.dismiss()
                        onDismiss()
                    },
                    tint = MainColors.onSuccessSnackbar
                )
            },
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                .border(
                    width = 1.dp,
                    shape = MainShapes.snackbar,
                    color = MainColors.successSnackbarBorder
                )
                .then(modifier),
            content = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.success_icon),
                        contentDescription = stringResource(R.string.success),
                        modifier = Modifier
                    )
                    Spacer(modifier = Modifier.size(12.dp))
                    Text(
                        text = text,
                        color = MainColors.onSuccessSnackbar,
                        style = MainTypography.snackbar
                    )
                }
            },
            containerColor = MainColors.successSnackbar,
            shape = MainShapes.snackbar,
        )
    }

    LaunchedEffect(showSnackbar) {
        if (showSnackbar) snackScope.launch {
            snackState.showSnackbar(
                message = text,
                duration = SnackbarDuration.Long
            )
            onShown()
        }
    }
}

@Preview
@Composable
fun SampleSnackbars() {
    MainTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Bottom
        ) {
            SuccessSnackbar(
                text = "A successful snackbar",
                onDismiss = {},
                showSnackbar = true,
                onShown = {}
            )
        }
    }
}