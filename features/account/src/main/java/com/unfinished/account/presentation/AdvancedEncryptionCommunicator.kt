package com.unfinished.account.presentation

import android.os.Parcelable
import com.unfinished.account.data.mappers.mapAdvancedEncryptionResponseToAdvancedEncryption
import com.unfinished.account.data.mappers.mapAdvancedEncryptionStateToResponse
import com.unfinished.account.domain.account.advancedEncryption.AdvancedEncryption
import com.unfinished.account.domain.interactor.AdvancedEncryptionInteractor
import com.unfinished.account.presentation.model.account.AdvancedEncryptionPayload
import com.unfinished.account.presentation.model.account.add.AddAccountPayload
import com.unfinished.common.navigation.InterScreenRequester
import com.unfinished.common.navigation.InterScreenResponder
import com.unfinished.data.model.CryptoType
import kotlinx.android.parcel.Parcelize

interface AdvancedEncryptionRequester : InterScreenRequester<AdvancedEncryptionPayload, AdvancedEncryptionCommunicator.Response>

suspend fun AdvancedEncryptionRequester.lastResponseOrDefault(addAccountPayload: AddAccountPayload, using: AdvancedEncryptionInteractor): AdvancedEncryptionCommunicator.Response {
    return latestResponse ?: mapAdvancedEncryptionStateToResponse(
        using.getInitialInputState(
            AdvancedEncryptionPayload.Change(addAccountPayload)
        )
    )
}

suspend fun AdvancedEncryptionRequester.lastAdvancedEncryptionOrDefault(
    addAccountPayload: AddAccountPayload,
    using: AdvancedEncryptionInteractor
): AdvancedEncryption {
    return mapAdvancedEncryptionResponseToAdvancedEncryption(lastResponseOrDefault(addAccountPayload, using))
}

interface AdvancedEncryptionResponder : InterScreenResponder<AdvancedEncryptionPayload, AdvancedEncryptionCommunicator.Response>

interface AdvancedEncryptionCommunicator : AdvancedEncryptionRequester, AdvancedEncryptionResponder {

    @Parcelize
    class Response(
        val substrateCryptoType: CryptoType?,
        val substrateDerivationPath: String?,
        val ethereumCryptoType: CryptoType?,
        val ethereumDerivationPath: String?
    ) : Parcelable
}
