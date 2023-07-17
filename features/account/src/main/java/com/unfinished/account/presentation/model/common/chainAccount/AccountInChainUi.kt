package com.unfinished.account.presentation.model.common.chainAccount

import android.graphics.drawable.Drawable
import com.unfinished.account.presentation.model.chain.ChainUi

class AccountInChainUi(
    val chainUi: ChainUi,
    val addressOrHint: String,
    val address: String?,
    val accountIcon: Drawable,
    val actionsAvailable: Boolean
)
