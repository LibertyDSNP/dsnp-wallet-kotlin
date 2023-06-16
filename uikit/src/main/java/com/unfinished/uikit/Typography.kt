package com.unfinished.uikit

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object MainTypography {

    private val poppinsTextStyle = TextStyle(fontFamily = poppins)
    private val spaceMonoTextStyle = TextStyle(fontFamily = spaceMono)
    private val nunitoTextStyle = TextStyle(fontFamily = nunito)

    val title: TextStyle = poppinsTextStyle.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        lineHeight = 28.sp
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

    val profile = poppinsTextStyle.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 20.sp,
    )

    val rowHeader = spaceMonoTextStyle.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = 18.sp
    )

    val textLink = nunitoTextStyle.copy (
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        lineHeight = 13.sp
    )
}

val mainTypography = Typography()