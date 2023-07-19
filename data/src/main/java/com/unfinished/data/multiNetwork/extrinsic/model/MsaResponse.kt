package com.unfinished.data.multiNetwork.extrinsic.model

import com.unfinished.data.model.event.EventType
import org.bouncycastle.util.BigIntegers
import java.math.BigInteger

data class MsaResponse(
    val msaEvent: EventType.MsaEvent?
)