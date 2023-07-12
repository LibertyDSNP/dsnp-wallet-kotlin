package com.unfinished.feature_account.domain.model

import com.unfinished.data.api.model.CryptoType

class ImportJsonMetaData(
    val name: String?,
    val chainId: String?,
    val encryptionType: CryptoType
)
