package com.unfinished.common.utils

import android.os.SystemClock
import android.view.View

/**
 * This class handles click events to avoid multiple clicks
 *
 */
class SafeClickListener(
    var timeGap: Long = 1000L,
    val block: (View) -> Unit,
) : View.OnClickListener {
    private var lastTimeClicked: Long = 0
    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < timeGap) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        block(v)
    }
}