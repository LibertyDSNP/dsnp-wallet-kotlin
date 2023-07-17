package com.unfinished.account.data.signer

import com.unfinished.account.domain.model.MetaAccount
import com.unfinished.runtime.multiNetwork.chain.model.Chain
import jp.co.soramitsu.fearless_utils.runtime.extrinsic.signer.Signer

interface SignerProvider {

    fun signerFor(metaAccount: MetaAccount): Signer

    fun feeSigner(chain: Chain): Signer
}
