package io.novafoundation.nova.common.vibration

import android.annotation.SuppressLint
import android.os.Vibrator

class DeviceVibrator(
    private val vibrator: Vibrator
) {

    companion object {
        private const val SHORT_VIBRATION_DURATION = 200L
    }

    /**
     * TODO: Looks like we need to check for permissions for vibrate. Seems like this api
     * is deprecated and we should swap it out.
     */
    @SuppressLint("MissingPermission")
    fun makeShortVibration() {
        vibrator.vibrate(SHORT_VIBRATION_DURATION)
    }
}
