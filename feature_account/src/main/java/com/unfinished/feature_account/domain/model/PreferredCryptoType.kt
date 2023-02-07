package com.unfinished.feature_account.domain.model

import io.novafoundation.nova.core.model.CryptoType

data class PreferredCryptoType(
    val cryptoType: CryptoType,
    val frozen: Boolean
)
