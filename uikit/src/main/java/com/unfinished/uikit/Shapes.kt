package com.unfinished.uikit

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

object MainShapes {

    val button: Shape
        @Composable get() = ButtonDefaults.shape

    val snackbar: Shape
        @Composable get() = RoundedCornerShape(10.dp)

    val dialog: Shape
        @Composable get() = RoundedCornerShape(40.dp)
}