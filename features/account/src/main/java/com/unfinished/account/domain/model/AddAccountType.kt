package com.unfinished.account.domain.model

import com.unfinished.data.multiNetwork.chain.model.ChainId

sealed class AddAccountType {

    class MetaAccount(val name: String) : AddAccountType()

    class ChainAccount(val chainId: ChainId, val metaId: Long) : AddAccountType()
}
