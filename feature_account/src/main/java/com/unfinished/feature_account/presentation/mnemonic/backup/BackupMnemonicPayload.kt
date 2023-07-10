package com.unfinished.feature_account.presentation.mnemonic.backup

import android.os.Parcelable
import com.unfinished.feature_account.presentation.model.account.add.AddAccountPayload
import com.unfinished.runtime.multiNetwork.chain.model.ChainId
import kotlinx.android.parcel.Parcelize

sealed class BackupMnemonicPayload : Parcelable {

    @Parcelize
    class Create(
        val newWalletName: String?,
        val addAccountPayload: AddAccountPayload
    ) : BackupMnemonicPayload()

    @Parcelize
    class Confirm(
        val chainId: ChainId,
        val metaAccountId: Long
    ) : BackupMnemonicPayload()
}
