package com.unfinished.uikit.components

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheet(
    showBottomSheet: Boolean,
    sheetContent: @Composable ColumnScope.() -> Unit,
    content: @Composable () -> Unit,
    backPress: () -> Unit,
    onHidden: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )

    val roundedCornerRadius = 12.dp

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(
            topStart = roundedCornerRadius,
            topEnd = roundedCornerRadius
        ),
        sheetContent = sheetContent,
        content = content
    )

    BackHandler(modalBottomSheetState.isVisible) {
        backPress()
    }

    if(modalBottomSheetState.currentValue == ModalBottomSheetValue.Hidden){
        LaunchedEffect(modalBottomSheetState.currentValue) {
            if(showBottomSheet) onHidden()
        }
    }

    LaunchedEffect(showBottomSheet) {
        coroutineScope.launch {
            if (showBottomSheet) modalBottomSheetState.show() else modalBottomSheetState.hide()
        }
    }
}