package com.unfinished.feature_account.domain.account.export.seed

import com.unfinished.common.data.secrets.v2.SecretStoreV2
import com.unfinished.common.data.secrets.v2.seed
import com.unfinished.feature_account.data.secrets.getAccountSecrets
import com.unfinished.feature_account.domain.interfaces.AccountRepository
import com.unfinished.runtime.multiNetwork.ChainRegistry
import com.unfinished.runtime.multiNetwork.chain.model.ChainId
import jp.co.soramitsu.fearless_utils.extensions.toHexString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExportSeedInteractor(
    private val accountRepository: AccountRepository,
    private val secretStoreV2: SecretStoreV2,
    private val chainRegistry: ChainRegistry,
) {

    suspend fun getAccountSeed(
        metaId: Long,
        chainId: ChainId,
    ): String = withContext(Dispatchers.Default) {
        val metaAccount = accountRepository.getMetaAccount(metaId)
        val chain = chainRegistry.getChain(chainId)

        val accountSecrets = secretStoreV2.getAccountSecrets(metaAccount, chain)

        accountSecrets.seed()?.toHexString(withPrefix = true)
            ?: error("No seed found for account ${metaAccount.name} in ${chain.name}")
    }
}
