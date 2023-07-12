package com.unfinished.account.domain.model

import com.unfinished.data.model.CryptoType

data class PreferredCryptoType(
    val cryptoType: CryptoType,
    val frozen: Boolean
)
