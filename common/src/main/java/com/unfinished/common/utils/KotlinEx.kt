package com.unfinished.common.utils

import android.content.Context
import com.unfinished.common.R
import java.io.InputStream
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.time.Duration


fun Long.daysFromMillis() = TimeUnit.MILLISECONDS.toDays(this)

fun InputStream.readText() = bufferedReader().use { it.readText() }

fun Long.formatDateTime() = dateTimeFormat.format(Date(this))

private val dateTimeFormat = SimpleDateFormat.getDateTimeInstance()

fun Duration.format(
    estimated: Boolean,
    context: Context,
    timeFormat: TimeFormatter?
): String = format(
    estimated = estimated,
    daysFormat = { context.resources.getQuantityString(R.plurals.staking_main_lockup_period_value, it, it) },
    hoursFormat = { context.resources.getQuantityString(R.plurals.common_hours_format, it, it) },
    minutesFormat = { context.resources.getQuantityString(R.plurals.common_minutes_format, it, it) },
    timeFormat = timeFormat
)

typealias TimeFormatter = (hours: Int, minutes: Int, seconds: Int) -> String

inline fun Duration.format(
    estimated: Boolean,
    daysFormat: (days: Int) -> String,
    hoursFormat: (hours: Int) -> String,
    minutesFormat: (minutes: Int) -> String,
    noinline timeFormat: TimeFormatter?
): String {
    val withoutPrefix = toComponents { days, hours, minutes, seconds, _ ->
        when {
            // format days + hours if both are present
            days > 0 && hours > 0 -> "${daysFormat(days.toInt())} ${hoursFormat(hours)}"
            // only days in case there is no hours
            days > 0 -> daysFormat(days.toInt())
            // if timeFormat is given, format with it in case there is less then 1 day left
            timeFormat != null -> timeFormat(hours, minutes, seconds)
            // format hours if present
            hours > 0 -> hoursFormat(hours)
            // format minutes otherwise
            else -> minutesFormat(minutes)
        }
    }

    return if (estimated) {
        "~$withoutPrefix"
    } else {
        withoutPrefix
    }
}

fun <K, V> Map<K, V>.reversed() = HashMap<V, K>().also { newMap ->
    entries.forEach { newMap[it.value] = it.key }
}

fun Int.quantize(factor: Int) = this - this % factor


fun <T> Result<T>.requireException() = exceptionOrNull()!!

fun <T> Result<T>.requireValue() = getOrThrow()!!

private val PERCENTAGE_MULTIPLIER = 100.toBigDecimal()

fun BigDecimal.fractionToPercentage() = this * PERCENTAGE_MULTIPLIER

val BigDecimal.isNonNegative: Boolean
    get() = signum() >= 0