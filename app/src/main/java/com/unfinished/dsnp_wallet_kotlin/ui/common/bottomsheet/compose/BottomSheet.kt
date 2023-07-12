package com.unfinished.dsnp_wallet_kotlin.ui.common.bottomsheet.compose

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.unfinished.dsnp_wallet_kotlin.ui.common.bottomsheet.viewmodel.BottomSheetViewModel


@Composable
fun BottomSheet(
    bottomSheetViewModel: BottomSheetViewModel = hiltViewModel(),
    sheetContent: @Composable ColumnScope.(BottomSheetViewModel.State) -> Unit,
    content: @Composable () -> Unit,
    backPress: () -> Unit
) {
    val bottomSheetVisibleStateFlow = bottomSheetViewModel.stateFlow.collectAsState()
    val bottomSheetVisibleState = bottomSheetVisibleStateFlow.value

    com.unfinished.uikit.components.BottomSheet(
        showBottomSheet = bottomSheetVisibleState != BottomSheetViewModel.State.Hide,
        sheetContent = {
            sheetContent(bottomSheetVisibleState)
        },
        content = content,
        backPress = backPress,
        onHidden = { bottomSheetViewModel.hide() }
    )
}