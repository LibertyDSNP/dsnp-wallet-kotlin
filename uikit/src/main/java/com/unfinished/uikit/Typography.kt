package com.unfinished.uikit

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object MainTypography {

    private val poppinsTextStyle = TextStyle(fontFamily = poppins)

    val title: TextStyle = poppinsTextStyle.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = (-2).sp
    )

    val body = poppinsTextStyle.copy(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 20.sp
    )

    val bodyMedium = poppinsTextStyle.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 24.sp,
    )
}

val mainTypography = Typography()