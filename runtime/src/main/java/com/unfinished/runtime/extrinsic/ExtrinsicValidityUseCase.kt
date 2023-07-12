package com.unfinished.runtime.extrinsic

import com.unfinished.data.util.formating.TimerValue
import com.unfinished.data.util.formating.remainingTime
import jp.co.soramitsu.fearless_utils.runtime.extrinsic.signer.SignerPayloadExtrinsic
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

class ValidityPeriod(val period: TimerValue)

fun ValidityPeriod.remainingTime(): Long {
    return period.remainingTime()
}

fun ValidityPeriod.closeToExpire(): Boolean {
    return remainingTime().milliseconds < 1.minutes
}

fun ValidityPeriod.ended(): Boolean {
    return remainingTime() == 0L
}

interface ExtrinsicValidityUseCase {

    suspend fun extrinsicValidityPeriod(payload: SignerPayloadExtrinsic): ValidityPeriod
}

internal class RealExtrinsicValidityUseCase(
    private val mortalityConstructor: MortalityConstructor
) : ExtrinsicValidityUseCase {

    override suspend fun extrinsicValidityPeriod(payload: SignerPayloadExtrinsic): ValidityPeriod {
        val timerValue = TimerValue(
            millis = mortalityConstructor.mortalPeriodMillis(),
            millisCalculatedAt = System.currentTimeMillis()
        )

        return ValidityPeriod(timerValue)
    }
}
//TODO need to check If it's any of use
//fun LifecycleOwner.startExtrinsicValidityTimer(
//    validityPeriod: ValidityPeriod,
//    @StringRes timerFormat: Int,
//    timerView: TextView,
//    onTimerFinished: () -> Unit
//) {
//    timerView.startTimer(
//        value = validityPeriod.period,
//        customMessageFormat = timerFormat,
//        lifecycle = lifecycle,
//        onTick = { view, _ ->
//            val textColorRes = if (validityPeriod.closeToExpire()) R.color.text_negative else R.color.text_secondary
//
//            view.setTextColorRes(textColorRes)
//        },
//        onFinish = { onTimerFinished() }
//    )
//}
