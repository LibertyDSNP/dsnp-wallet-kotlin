package com.unfinished.account.presentation.wallet

import android.graphics.drawable.Drawable
import com.unfinished.account.domain.model.MetaAccount
import kotlinx.coroutines.flow.Flow

class WalletModel(val name: String, val icon: Drawable?)

interface WalletUiUseCase {

    fun selectedWalletUiFlow(showAddressIcon: Boolean = false): Flow<WalletModel>

    suspend fun selectedWalletUi(): WalletModel

    suspend fun walletIcon(metaAccount: MetaAccount, transparentBackground: Boolean = true): Drawable
}
