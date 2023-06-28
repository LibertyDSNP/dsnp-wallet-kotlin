package com.unfinished.uikit.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Grid(
    modifier: Modifier = Modifier,
    columns: Int,
    itemCount: Int,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    content: @Composable() (Int) -> Unit
) {
    val offSet = if (itemCount.mod(columns) > 0) 1 else 0
    val rows = (itemCount / columns) + offSet

    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement
    ) {
        for (rowIndex in 0 until rows) {
            val startingIndex = rowIndex * columns

            Row(
                horizontalArrangement = horizontalArrangement,
            ) {
                for (columnIndex in 0 until columns) {
                    val itemIndex = startingIndex + columnIndex
                    Box(
                        modifier = Modifier.weight(1F)
                    ) {
                        if (itemIndex < itemCount) content(itemIndex)
                    }
                }
            }
        }
    }
}