package com.unfinished.uikit

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object MainTypography {

    private val poppinsTextStyle = TextStyle(fontFamily = poppins)
    private val spaceMonoTextStyle = TextStyle(fontFamily = spaceMono)
    private val nunitoTextStyle = TextStyle(fontFamily = nunito)
    private val workSansTextStyle = TextStyle(fontFamily = workSans)

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

    val bodySemiBold = poppinsTextStyle.copy(
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 20.sp
    )

    val bodyMedium = poppinsTextStyle.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 24.sp,
    )

    val bodyMediumBold = poppinsTextStyle.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 24.sp,
    )

    val bodySmall = poppinsTextStyle.copy(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp
    )

    val bodyLarge = poppinsTextStyle.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,
        lineHeight = 24.sp
    )

    val bodyLargeBold = poppinsTextStyle.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 24.sp
    )

    val profile = poppinsTextStyle.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 20.sp,
    )

    val profileSecondary = poppinsTextStyle.copy(
        fontWeight = FontWeight.Light,
        fontSize = 16.sp,
        lineHeight = 20.sp,
    )

    val rowHeader = spaceMonoTextStyle.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = 18.sp
    )

    val textLink = nunitoTextStyle.copy(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 13.sp
    )

    val bottomBar = workSansTextStyle.copy(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 12.sp,
        letterSpacing = -(.24).sp
    )

    val stepCounter = poppinsTextStyle.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 20.sp
    )

    val toolbarTitle = poppinsTextStyle.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = 18.sp
    )

    val largeButtonText = poppinsTextStyle.copy(
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 24.sp
    )

    val seedText = spaceMonoTextStyle.copy(
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 22.sp
    )

    val seedTextBold = spaceMonoTextStyle.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        lineHeight = 22.sp
    )

    val snackbar = poppinsTextStyle.copy(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    )

    val dialogTitle: TextStyle = poppinsTextStyle.copy(
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 24.sp
    )

}

val mainTypography = Typography()