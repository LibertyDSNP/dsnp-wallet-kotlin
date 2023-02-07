package com.unfinished.feature_account.presentation.model.mnemonic

import android.os.Parcelable
import com.unfinished.feature_account.presentation.AdvancedEncryptionCommunicator
import com.unfinished.feature_account.presentation.model.account.add.AddAccountPayload
import kotlinx.android.parcel.Parcelize

@Parcelize
class ConfirmMnemonicPayload(
    val mnemonic: List<String>,
    val createExtras: CreateExtras?
) : Parcelable {
    @Parcelize
    class CreateExtras(
        val accountName: String?,
        val addAccountPayload: AddAccountPayload,
        val advancedEncryptionPayload: AdvancedEncryptionCommunicator.Response
    ) : Parcelable
}
