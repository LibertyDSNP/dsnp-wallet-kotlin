package com.unfinished.data.mappers

import com.unfinished.data.model.CryptoType
import jp.co.soramitsu.fearless_utils.encrypt.EncryptionType

fun mapCryptoTypeToEncryption(cryptoType: CryptoType): EncryptionType {
    return when (cryptoType) {
        CryptoType.SR25519 -> EncryptionType.SR25519
        CryptoType.ED25519 -> EncryptionType.ED25519
        CryptoType.ECDSA -> EncryptionType.ECDSA
    }
}

fun mapEncryptionToCryptoType(cryptoType: EncryptionType): CryptoType {
    return when (cryptoType) {
        EncryptionType.SR25519 -> CryptoType.SR25519
        EncryptionType.ED25519 -> CryptoType.ED25519
        EncryptionType.ECDSA -> CryptoType.ECDSA
    }
}
