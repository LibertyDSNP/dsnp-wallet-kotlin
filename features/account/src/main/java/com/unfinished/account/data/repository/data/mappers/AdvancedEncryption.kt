package com.unfinished.account.data.repository.data.mappers

import com.unfinished.account.domain.advancedEncryption.AdvancedEncryption
import com.unfinished.account.domain.advancedEncryption.AdvancedEncryptionInput
import com.unfinished.account.presentation.AdvancedEncryptionCommunicator
import com.unfinished.common.utils.input.valueOrNull

fun mapAdvancedEncryptionStateToResponse(
    input: AdvancedEncryptionInput
): AdvancedEncryptionCommunicator.Response {
    return with(input) {
        AdvancedEncryptionCommunicator.Response(
            substrateCryptoType = substrateCryptoType.valueOrNull,
            substrateDerivationPath = substrateDerivationPath.valueOrNull,
            ethereumCryptoType = ethereumCryptoType.valueOrNull,
            ethereumDerivationPath = ethereumDerivationPath.valueOrNull
        )
    }
}

fun mapAdvancedEncryptionResponseToAdvancedEncryption(
    advancedEncryptionResponse: AdvancedEncryptionCommunicator.Response
): AdvancedEncryption {
    return with(advancedEncryptionResponse) {
        AdvancedEncryption(
            substrateCryptoType = substrateCryptoType,
            ethereumCryptoType = ethereumCryptoType,
            derivationPaths = AdvancedEncryption.DerivationPaths(
                substrate = substrateDerivationPath,
                ethereum = ethereumDerivationPath
            )
        )
    }
}
