package com.unfinished.feature_account.domain.model

import com.unfinished.data.api.model.CryptoType

data class PreferredCryptoType(
    val cryptoType: CryptoType,
    val frozen: Boolean
)
