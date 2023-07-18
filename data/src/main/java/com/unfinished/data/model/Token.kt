package com.unfinished.data.model

import com.unfinished.data.multiNetwork.chain.model.Chain
import com.unfinished.data.util.amountFromPlanks
import com.unfinished.data.util.planksFromAmount
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


