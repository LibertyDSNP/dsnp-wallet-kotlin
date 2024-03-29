package com.unfinished.account.domain.interfaces

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import com.unfinished.common.R
import com.unfinished.account.domain.model.LightMetaAccount
import com.unfinished.account.domain.model.MetaAccount
import com.unfinished.account.presentation.icon.createAccountAddressModel
import com.unfinished.account.presentation.wallet.WalletUiUseCase
import com.unfinished.common.address.AddressIconGenerator
import com.unfinished.runtime.multiNetwork.chain.model.Chain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SelectedWalletModel(
    @DrawableRes val typeIcon: Int?,
    val walletIcon: Drawable,
    val name: String,
)

class SelectedAccountUseCase(
    private val accountRepository: AccountRepository,
    private val walletUiUseCase: WalletUiUseCase,
    private val addressIconGenerator: AddressIconGenerator,
) {

    fun selectedMetaAccountFlow(): Flow<MetaAccount> = accountRepository.selectedMetaAccountFlow()

    fun selectedAddressModelFlow(chain: suspend () -> Chain) = selectedMetaAccountFlow().map {
        addressIconGenerator.createAccountAddressModel(
            chain = chain(),
            account = it,
            name = null
        )
    }

    fun selectedWalletModelFlow(): Flow<SelectedWalletModel> = selectedMetaAccountFlow().map {
        val icon = walletUiUseCase.walletIcon(it, transparentBackground = false)

        val typeIcon = when (it.type) {
            LightMetaAccount.Type.SECRETS -> null // no icon for secrets account
            LightMetaAccount.Type.WATCH_ONLY -> R.drawable.ic_watch_only_filled
            LightMetaAccount.Type.PARITY_SIGNER -> R.drawable.ic_parity_signer
            LightMetaAccount.Type.LEDGER -> R.drawable.ic_ledger
        }

        SelectedWalletModel(
            typeIcon = typeIcon,
            walletIcon = icon,
            name = it.name
        )
    }

    suspend fun getSelectedMetaAccount(): MetaAccount = accountRepository.getSelectedMetaAccount()
}
