package com.unfinished.dsnp_wallet_kotlin.ui.dialog.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import com.unfinished.dsnp_wallet_kotlin.ui.dialog.viewmodel.DialogViewModel
import com.unfinished.dsnp_wallet_kotlin.ui.home.compose.CongratulationsScreen

@Composable
fun CloseableDialog(
    navController: NavHostController,
    dialogViewModel: DialogViewModel
) {
    val dialogVisibleStateFlow = dialogViewModel.stateFlow.collectAsState()
    val dialogVisibleState = dialogVisibleStateFlow.value

    if (dialogVisibleState != DialogViewModel.State.Hide) com.unfinished.uikit.components.CloseableDialog(
        content = {
            when (dialogVisibleState) {
                DialogViewModel.State.Hide -> {}
                is DialogViewModel.State.Congratulation -> CongratulationsScreen(
                    username = dialogVisibleState.userName,
                    letsGoClick = {
                        dialogViewModel.hide()
                        navController.navigate(dialogVisibleState.letsGoDirection.route)
                    },
                    onDismiss = { dialogViewModel.hide() }
                )

            }
        },
        onDismiss = {
            dialogViewModel.hide()
        }
    )
}