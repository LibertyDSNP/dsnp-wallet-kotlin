package com.unfinished.feature_account.data.signer

import com.unfinished.feature_account.domain.model.MetaAccount
import io.novafoundation.nova.runtime.multiNetwork.chain.model.Chain
import jp.co.soramitsu.fearless_utils.runtime.extrinsic.signer.Signer

interface SignerProvider {

    fun signerFor(metaAccount: MetaAccount): Signer

    fun feeSigner(chain: Chain): Signer
}
