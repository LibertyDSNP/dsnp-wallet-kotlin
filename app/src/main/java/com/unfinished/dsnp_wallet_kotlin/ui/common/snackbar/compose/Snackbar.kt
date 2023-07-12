package com.unfinished.dsnp_wallet_kotlin.ui.common.snackbar.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.unfinished.dsnp_wallet_kotlin.ui.common.snackbar.viewmodel.SnackbarViewModel
import com.unfinished.uikit.components.SuccessSnackbar

@Composable
fun Snackbar(
    modifier: Modifier = Modifier,
    snackbarViewModel: SnackbarViewModel
) {
    val snackbarStateFlow = snackbarViewModel.stateFlow.collectAsState()

    when (snackbarStateFlow.value) {
        SnackbarViewModel.State.Hide -> {}
        is SnackbarViewModel.State.ShowSuccess -> SuccessSnackbar(
            modifier = modifier,
            snackbarViewModel = snackbarViewModel
        )
    }
}

@Composable
private fun SuccessSnackbar(
    modifier: Modifier = Modifier,
    snackbarViewModel: SnackbarViewModel
) {
    val snackbarStateFlow = snackbarViewModel.stateFlow.collectAsState()

    SuccessSnackbar(
        text = snackbarStateFlow.value.text,
        showSnackbar = snackbarStateFlow.value != SnackbarViewModel.State.Hide,
        modifier = modifier,
        onDismiss = { snackbarViewModel.hide() },
        onShown = { snackbarViewModel.hide() }
    )
}