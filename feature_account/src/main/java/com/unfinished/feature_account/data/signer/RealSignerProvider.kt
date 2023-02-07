package com.unfinished.feature_account.data.signer

import com.unfinished.feature_account.domain.model.LightMetaAccount
import com.unfinished.feature_account.domain.model.MetaAccount
import com.unfinished.feature_account.data.signer.secrets.SecretsSignerFactory
import io.novafoundation.nova.runtime.extrinsic.FeeSigner
import io.novafoundation.nova.runtime.multiNetwork.chain.model.Chain
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
