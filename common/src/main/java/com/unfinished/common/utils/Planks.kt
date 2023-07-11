package com.unfinished.common.utils

import java.math.BigDecimal
import java.math.BigInteger

fun BigDecimal.planksFromAmount(precision: Int) = this.scaleByPowerOfTen(precision).toBigInteger()
fun BigInteger.amountFromPlanks(precision: Int) = toBigDecimal(scale = precision)

//https://github.com/LibertyDSNP/frequency-docs/blob/editing-session/pages/Tokenomics/TokenomicsOverview.md#frqcy-token--capacity
fun Float.toPlanks(): BigInteger =  (this * 100000000).toBigDecimal().toBigInteger()

fun BigInteger.toUnit(): Float =  this.toFloat() / 100000000