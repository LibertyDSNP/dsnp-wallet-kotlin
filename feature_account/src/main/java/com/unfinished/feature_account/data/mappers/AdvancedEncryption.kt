package com.unfinished.feature_account.data.mappers

import com.unfinished.feature_account.domain.account.advancedEncryption.AdvancedEncryption
import com.unfinished.feature_account.domain.account.advancedEncryption.AdvancedEncryptionInput
import com.unfinished.feature_account.presentation.AdvancedEncryptionCommunicator
import io.novafoundation.nova.common.utils.input.valueOrNull

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
