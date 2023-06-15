package com.unfinished.uikit.exts

import android.content.Context
import android.widget.Toast

/**
 * This is just a dev toast message for any features not ready
 */
fun Context.comingSoonToast() {
    Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show()
}