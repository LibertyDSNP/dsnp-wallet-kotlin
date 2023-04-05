package com.unfinished.feature_account.domain.model

import io.novafoundation.nova.core.model.CryptoType

class ImportJsonMetaData(
    val name: String?,
    val chainId: String?,
    val encryptionType: CryptoType
)
