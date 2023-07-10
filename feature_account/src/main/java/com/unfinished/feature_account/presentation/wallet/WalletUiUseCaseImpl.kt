package com.unfinished.feature_account.presentation.wallet

import android.graphics.drawable.Drawable
import com.unfinished.feature_account.domain.interfaces.AccountRepository
import com.unfinished.feature_account.domain.model.MetaAccount
import com.unfinished.common.address.AddressIconGenerator
import com.unfinished.common.address.AddressIconGenerator.Companion.BACKGROUND_DEFAULT
import com.unfinished.common.address.AddressIconGenerator.Companion.BACKGROUND_TRANSPARENT
import com.unfinished.common.utils.ByteArrayComparator
import jp.co.soramitsu.fearless_utils.runtime.AccountId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

class WalletUiUseCaseImpl(
    private val accountRepository: AccountRepository,
    private val addressIconGenerator: AddressIconGenerator,
) : WalletUiUseCase {

    override fun selectedWalletUiFlow(
        showAddressIcon: Boolean
    ): Flow<WalletModel> {
        return accountRepository.selectedMetaAccountFlow().mapLatest { metaAccount ->
            val icon = maybeGenerateIcon(accountId = metaAccount.walletIconSeed(), shouldGenerate = showAddressIcon)

            WalletModel(
                name = metaAccount.name,
                icon = icon
            )
        }
    }

    override suspend fun selectedWalletUi(): WalletModel {
        val metaAccount = accountRepository.getSelectedMetaAccount()

        return WalletModel(
            name = metaAccount.name,
            icon = walletIcon(metaAccount)
        )
    }

    override suspend fun walletIcon(
        metaAccount: MetaAccount,
        transparentBackground: Boolean
    ): Drawable {
        val seed = metaAccount.walletIconSeed()

        return generateWalletIcon(seed, transparentBackground)
    }

    private suspend fun maybeGenerateIcon(accountId: AccountId, shouldGenerate: Boolean): Drawable? {
        return if (shouldGenerate) {
            generateWalletIcon(seed = accountId, transparentBackground = true)
        } else {
            null
        }
    }

    private suspend fun generateWalletIcon(seed: ByteArray, transparentBackground: Boolean): Drawable {
        return addressIconGenerator.createAddressIcon(
            accountId = seed,
            sizeInDp = AddressIconGenerator.SIZE_MEDIUM,
            backgroundColorRes = if (transparentBackground) BACKGROUND_TRANSPARENT else BACKGROUND_DEFAULT
        )
    }

    private fun MetaAccount.walletIconSeed(): ByteArray {
        return when {
            substrateAccountId != null -> substrateAccountId!!
            ethereumAddress != null -> ethereumAddress!!

            // if both default accounts are null there MUST be at least one chain account. Otherwise wallet is in invalid state
            else -> {
                chainAccounts.values
                    .map(MetaAccount.ChainAccount::accountId)
                    .sortedWith(ByteArrayComparator())
                    .first()
            }
        }
    }
}
