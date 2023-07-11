package com.unfinished.feature_account.domain.account.details

import com.unfinished.common.data.secrets.v2.SecretStoreV2
import com.unfinished.common.data.secrets.v2.entropy
import com.unfinished.common.data.secrets.v2.getAccountSecrets
import com.unfinished.common.data.secrets.v2.seed
import com.unfinished.common.utils.mapToSet
import com.unfinished.feature_account.domain.interfaces.AccountRepository
import com.unfinished.feature_account.domain.model.LightMetaAccount
import com.unfinished.feature_account.domain.model.MetaAccount
import com.unfinished.feature_account.domain.model.accountIdIn
import com.unfinished.feature_account.domain.model.addressIn
import com.unfinished.feature_account.domain.model.hasChainAccountIn
import com.unfinished.feature_account.domain.account.details.AccountInChain.From
import com.unfinished.feature_account.domain.validation.SubstrateApplicationConfig
import com.unfinished.feature_account.presentation.model.account.add.SecretType
import com.unfinished.runtime.ext.defaultComparatorFrom
import com.unfinished.runtime.multiNetwork.ChainRegistry
import com.unfinished.runtime.multiNetwork.chain.model.Chain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class AccountDetailsInteractor(
    private val accountRepository: AccountRepository,
    private val secretStoreV2: SecretStoreV2,
    private val chainRegistry: ChainRegistry,
) {

    suspend fun getMetaAccount(metaId: Long): MetaAccount {
        return accountRepository.getMetaAccount(metaId)
    }

    suspend fun updateName(metaId: Long, newName: String) {
        accountRepository.updateMetaAccountName(metaId, newName)
    }

    suspend fun availableExportTypes(
        metaAccount: MetaAccount,
        chain: Chain,
    ): Set<SecretType> = withContext(Dispatchers.Default) {
        val accountId = metaAccount.accountIdIn(chain) ?: return@withContext emptySet()

        val secrets = secretStoreV2.getAccountSecrets(metaAccount.id, accountId)

        setOfNotNull(
            SecretType.MNEMONIC.takeIf { secrets.entropy() != null },
            SecretType.SEED.takeIf { secrets.seed() != null },
            SecretType.JSON // always available
        )
    }

    private val AccountInChain.hasChainAccount
        get() = projection != null

    private fun accountInChainComparator(metaAccountType: LightMetaAccount.Type): Comparator<AccountInChain> {
        val hasAccountOrdering: Comparator<AccountInChain> = when (metaAccountType) {
            LightMetaAccount.Type.LEDGER -> compareBy { !it.hasChainAccount }
            else -> compareBy { it.hasChainAccount }
        }

        return hasAccountOrdering.then(Chain.defaultComparatorFrom(AccountInChain::chain))
    }

    private suspend fun shownChainsFor(metaAccountType: LightMetaAccount.Type): List<Chain> {
        val allChains = chainRegistry.currentChains.first()

        return when (metaAccountType) {
            LightMetaAccount.Type.LEDGER -> {
                val ledgerSupportedChainIds = SubstrateApplicationConfig.all().mapToSet { it.chainId }

                allChains.filter { it.id in ledgerSupportedChainIds }
            }
            else -> allChains
        }
    }
}

private val From.ordering
    get() = when (this) {
        From.CHAIN_ACCOUNT -> 0
        From.META_ACCOUNT -> 1
    }
