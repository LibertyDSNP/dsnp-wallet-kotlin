package com.unfinished.feature_account.domain.model

import com.unfinished.common.utils.amountFromPlanks
import com.unfinished.common.utils.planksFromAmount
import com.unfinished.runtime.multiNetwork.chain.model.Chain
import java.math.BigDecimal
import java.math.BigInteger

class Token(
    val rate: BigDecimal?,
    val currency: Currency,
    val recentRateChange: BigDecimal?,
    val configuration: Chain.Asset
) {
    // TODO move out of the class when Context Receivers will be stable
    fun BigDecimal.toPlanks() = planksFromAmount(this)
    fun BigInteger.toAmount() = amountFromPlanks(this)

    fun priceOf(tokenAmount: BigDecimal): BigDecimal = rate?.multiply(tokenAmount) ?: BigDecimal.ZERO
}

fun Token.amountFromPlanks(amountInPlanks: BigInteger) = configuration.amountFromPlanks(amountInPlanks)

fun Token.planksFromAmount(amount: BigDecimal): BigInteger = configuration.planksFromAmount(amount)

fun Chain.Asset.amountFromPlanks(amountInPlanks: BigInteger) = amountInPlanks.amountFromPlanks(precision)

fun Chain.Asset.planksFromAmount(amount: BigDecimal): BigInteger = amount.planksFromAmount(precision)

//https://github.com/LibertyDSNP/frequency-docs/blob/editing-session/pages/Tokenomics/TokenomicsOverview.md#frqcy-token--capacity
fun Float.toPlanks(): BigInteger =  (this * 100000000).toBigDecimal().toBigInteger()

fun BigInteger.toUnit(): Float =  this.toFloat() / 100000000

