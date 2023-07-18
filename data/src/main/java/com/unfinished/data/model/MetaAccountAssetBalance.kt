package com.unfinished.data.model

import java.math.BigDecimal
import java.math.BigInteger

class MetaAccountAssetBalance(
    val metaId: Long,
    val freeInPlanks: BigInteger,
    val reservedInPlanks: BigInteger,
    val offChainBalance: BigInteger?,
    val precision: Int,
    val rate: BigDecimal?
)
