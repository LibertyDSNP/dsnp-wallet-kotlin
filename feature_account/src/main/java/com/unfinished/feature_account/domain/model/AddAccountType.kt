package com.unfinished.feature_account.domain.model

import com.unfinished.runtime.multiNetwork.chain.model.ChainId

sealed class AddAccountType {

    class MetaAccount(val name: String) : AddAccountType()

    class ChainAccount(val chainId: ChainId, val metaId: Long) : AddAccountType()
}
