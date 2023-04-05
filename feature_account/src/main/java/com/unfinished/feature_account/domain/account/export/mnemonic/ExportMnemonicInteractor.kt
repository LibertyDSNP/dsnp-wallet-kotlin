package com.unfinished.feature_account.domain.account.export.mnemonic

import io.novafoundation.nova.common.data.secrets.v2.SecretStoreV2
import io.novafoundation.nova.common.data.secrets.v2.entropy
import com.unfinished.feature_account.data.secrets.getAccountSecrets
import com.unfinished.feature_account.domain.interfaces.AccountRepository
import io.novafoundation.nova.runtime.multiNetwork.ChainRegistry
import io.novafoundation.nova.runtime.multiNetwork.chain.model.ChainId
import jp.co.soramitsu.fearless_utils.encrypt.mnemonic.Mnemonic
import jp.co.soramitsu.fearless_utils.encrypt.mnemonic.MnemonicCreator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExportMnemonicInteractor(
    private val accountRepository: AccountRepository,
    private val secretStoreV2: SecretStoreV2,
    private val chainRegistry: ChainRegistry,
) {

    suspend fun getMnemonic(
        metaId: Long,
        chainId: ChainId,
    ): Mnemonic = withContext(Dispatchers.Default) {
        val metaAccount = accountRepository.getMetaAccount(metaId)
        val chain = chainRegistry.getChain(chainId)

        val entropy = secretStoreV2.getAccountSecrets(metaAccount, chain).entropy()
            ?: error("No mnemonic found for account ${metaAccount.name} in ${chain.name}")

        MnemonicCreator.fromEntropy(entropy)
    }
}
