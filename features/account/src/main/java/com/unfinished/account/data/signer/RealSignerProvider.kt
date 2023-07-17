package com.unfinished.account.data.signer

import com.unfinished.account.domain.model.LightMetaAccount
import com.unfinished.account.domain.model.MetaAccount
import com.unfinished.account.data.signer.secrets.SecretsSignerFactory
import com.unfinished.runtime.extrinsic.FeeSigner
import com.unfinished.runtime.multiNetwork.chain.model.Chain
import jp.co.soramitsu.fearless_utils.runtime.extrinsic.signer.Signer

internal class RealSignerProvider(
    private val secretsSignerFactory: SecretsSignerFactory,
) : SignerProvider {

    override fun signerFor(metaAccount: MetaAccount): Signer {
        return when (metaAccount.type) {
            LightMetaAccount.Type.SECRETS -> secretsSignerFactory.create(metaAccount)
            else -> {secretsSignerFactory.create(metaAccount)}
        }
    }

    override fun feeSigner(chain: Chain): Signer {
        return FeeSigner(chain)
    }
}
