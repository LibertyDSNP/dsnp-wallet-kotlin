package com.unfinished.uikit.exts

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import com.unfinished.uikit.MainColors
import com.unfinished.uikit.R

fun Context.launchChromeTab(
    url: String,
    showBackButton: Boolean = false
) {
    if (hasChrome()) {
        buildChromeTab(
            showBackButton = showBackButton
        ).build().launchUrl(this, url.toUri())
    } else {
        val intent = Intent(Intent.ACTION_VIEW).apply { data = url.toUri() }

        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, R.string.common_cannot_open_link, Toast.LENGTH_SHORT)
                .show()
        }
    }
}

private fun Context.hasChrome(): Boolean {
    val packageName = CustomTabsClient.getPackageName(
        this, emptyList<String>()
    )

    return !packageName.isNullOrBlank()
}

private fun Context.buildChromeTab(
    showBackButton: Boolean = false
): CustomTabsIntent.Builder {
    val builder = CustomTabsIntent.Builder()
        .setDefaultColorSchemeParams(
            CustomTabColorSchemeParams.Builder()
                .setToolbarColor(MainColors.toolbar.toArgb())
                .build()
        )
        .setShareState(CustomTabsIntent.SHARE_STATE_OFF)

    val closeButtonIconId = if (showBackButton) R.drawable.back_arrow else R.drawable.close
    AppCompatResources.getDrawable(this, closeButtonIconId)?.let {
        val wrappedDrawable = DrawableCompat.wrap(it)
        DrawableCompat.setTint(wrappedDrawable, MainColors.onToolbar.toArgb())

        builder.setCloseButtonIcon(wrappedDrawable.toBitmap())
    }

    return builder
}