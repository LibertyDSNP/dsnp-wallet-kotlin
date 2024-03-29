package com.unfinished.account.data.secrets

import com.unfinished.data.secrets.v2.AccountSecrets
import com.unfinished.data.secrets.v2.ChainAccountSecrets
import com.unfinished.data.secrets.v2.SecretStoreV2
import com.unfinished.data.secrets.v2.getAccountSecrets
import com.unfinished.data.secrets.v2.mapChainAccountSecretsToKeypair
import com.unfinished.data.secrets.v2.mapMetaAccountSecretsToDerivationPath
import com.unfinished.data.secrets.v2.mapMetaAccountSecretsToKeypair
import com.unfinished.data.util.fold
import jp.co.soramitsu.fearless_utils.encrypt.keypair.Keypair
import com.unfinished.account.domain.model.MetaAccount
import com.unfinished.account.domain.model.accountIdIn
import com.unfinished.runtime.multiNetwork.chain.model.Chain

suspend fun SecretStoreV2.getAccountSecrets(
    metaAccount: MetaAccount,
    chain: Chain
): AccountSecrets {
    val accountId = metaAccount.accountIdIn(chain) ?: error("No account for chain $chain in meta account ${metaAccount.name}")

    return getAccountSecrets(metaAccount.id, accountId)
}

fun AccountSecrets.keypair(chain: Chain): Keypair {
    return fold(
        left = { mapMetaAccountSecretsToKeypair(it, ethereum = chain.isEthereumBased) },
        right = { mapChainAccountSecretsToKeypair(it) }
    )
}

fun AccountSecrets.derivationPath(chain: Chain): String? {
    return fold(
        left = { mapMetaAccountSecretsToDerivationPath(it, ethereum = chain.isEthereumBased) },
        right = { it[ChainAccountSecrets.DerivationPath] }
    )
}
