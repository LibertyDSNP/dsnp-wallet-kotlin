package com.unfinished.common.view

import android.content.Context
import android.os.CountDownTimer
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.lifecycle.Lifecycle
import com.unfinished.common.R
import com.unfinished.common.utils.format
import com.unfinished.common.utils.onDestroy
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

private val TIMER_TAG = R.string.common_time_left

@OptIn(ExperimentalTime::class)
fun TextView.startTimer(
    millis: Long,
    millisCalculatedAt: Long? = null,
    lifecycle: Lifecycle? = null,
    @StringRes customMessageFormat: Int? = null,
    onTick: ((view: TextView, millisUntilFinished: Long) -> Unit)? = null,
    onFinish: ((view: TextView) -> Unit)? = null
) {
    val timePassedSinceCalculation = if (millisCalculatedAt != null) System.currentTimeMillis() - millisCalculatedAt else 0L

    val currentTimer = getTag(TIMER_TAG)

    if (currentTimer is CountDownTimer) {
        currentTimer.cancel()
    }

    val newTimer = object : CountDownTimer(millis - timePassedSinceCalculation, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            setNewValue(millisUntilFinished, customMessageFormat)

            onTick?.invoke(this@startTimer, millisUntilFinished)
        }

        override fun onFinish() {
            if (onFinish != null) {
                onFinish(this@startTimer)
            } else {
                this@startTimer.text = 0L.toDuration(DurationUnit.MILLISECONDS).formatTimer(context)
            }

            cancel()

            setTag(TIMER_TAG, null)
        }
    }

    lifecycle?.onDestroy {
        newTimer.cancel()
    }

    setNewValue(millis - timePassedSinceCalculation, customMessageFormat)
    newTimer.start()

    setTag(TIMER_TAG, newTimer)
}

private fun Duration.formatTimer(
    context: Context
) = format(
    estimated = false,
    context = context,
    timeFormat = { hours, minutes, seconds -> "%02d:%02d:%02d".format(hours, minutes, seconds) }
)

@OptIn(ExperimentalTime::class)
private fun TextView.setNewValue(mills: Long, timeFormatRes: Int?) {
    val formattedTime = mills.toDuration(DurationUnit.MILLISECONDS).formatTimer(context)

    val message = timeFormatRes?.let {
        resources.getString(timeFormatRes, formattedTime)
    } ?: formattedTime

    this.text = message
}

fun TextView.stopTimer() {
    val currentTimer = getTag(TIMER_TAG)

    if (currentTimer is CountDownTimer) {
        currentTimer.cancel()
        setTag(TIMER_TAG, null)
    }
}

