package com.unfinished.feature_account.domain.account.advancedEncryption

import com.unfinished.common.utils.input.Input
import com.unfinished.common.core.api.model.CryptoType

class AdvancedEncryptionInput(
    val substrateCryptoType: Input<CryptoType>,
    val substrateDerivationPath: Input<String>,
    val ethereumCryptoType: Input<CryptoType>,
    val ethereumDerivationPath: Input<String>
)

class AdvancedEncryption(
    val substrateCryptoType: CryptoType?,
    val ethereumCryptoType: CryptoType?,
    val derivationPaths: DerivationPaths
) {

    class DerivationPaths(
        val substrate: String?,
        val ethereum: String?
    ) {
        companion object {
            fun empty() = DerivationPaths(null, null)
        }
    }
}
