package com.unfinished.data.signer

import com.unfinished.data.multiNetwork.chain.model.Chain
import com.unfinished.data.signer.secrets.SecretsSignerFactory
import com.unfinished.data.multiNetwork.extrinsic.FeeSigner
import com.unfinished.data.model.account.LightMetaAccount
import com.unfinished.data.model.account.MetaAccount
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
