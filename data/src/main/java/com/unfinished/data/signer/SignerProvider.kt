package com.unfinished.data.signer

import com.unfinished.data.multiNetwork.chain.model.Chain
import com.unfinished.data.model.MetaAccount
import jp.co.soramitsu.fearless_utils.runtime.extrinsic.signer.Signer

interface SignerProvider {

    fun signerFor(metaAccount: MetaAccount): Signer

    fun feeSigner(chain: Chain): Signer
}
