package com.unfinished.feature_account.domain.account.export

class ExportingSecret<T>(
    val derivationPath: String?,
    val secret: T
)
