package com.unfinished.account.presentation.mnemonic.confirm

import android.os.Parcelable
import com.unfinished.account.presentation.AdvancedEncryptionCommunicator
import com.unfinished.account.presentation.model.account.add.AddAccountPayload
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
