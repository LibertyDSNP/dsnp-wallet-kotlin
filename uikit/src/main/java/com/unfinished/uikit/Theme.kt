package com.unfinished.uikit

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import com.google.android.material.color.MaterialColors
import com.unfinished.uikit.colors.DarkColors

var mainColors: Colors = DarkColors()
    private set

@Composable
fun MainTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {

    /**
     * TODO: Add logic here later when we are ready to support different sets of colors
     */
    mainColors = DarkColors()

    androidx.compose.material3.MaterialTheme(
        /**
         * TODO: Update [darkColorScheme] with the colors from [mainColors]. Google's color scheme
         * only contains high level properties and will not have all the details we would we need.
         */
        colorScheme = darkColorScheme(),
        typography = mainTypography,
        content = content
    )
}