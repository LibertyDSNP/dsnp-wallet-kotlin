package com.unfinished.account.domain.model

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


