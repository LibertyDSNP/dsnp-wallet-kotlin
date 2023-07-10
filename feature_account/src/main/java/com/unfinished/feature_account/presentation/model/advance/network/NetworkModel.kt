package com.unfinished.feature_account.presentation.model.advance.network

import com.unfinished.common.core.api.model.Node
import com.unfinished.common.core.api.model.Node.NetworkType.KUSAMA
import com.unfinished.common.core.api.model.Node.NetworkType.POLKADOT
import com.unfinished.common.core.api.model.Node.NetworkType.ROCOCO
import com.unfinished.common.core.api.model.Node.NetworkType.WESTEND
import com.unfinished.common.R

data class NetworkModel(
    val name: String,
    val networkTypeUI: NetworkTypeUI
) {
    sealed class NetworkTypeUI(val icon: Int, val networkType: Node.NetworkType) {
        object Kusama : NetworkTypeUI(R.drawable.ic_ksm_24, KUSAMA)
        object Polkadot : NetworkTypeUI(R.drawable.ic_polkadot_24, POLKADOT)
        object Westend : NetworkTypeUI(R.drawable.ic_westend_24, WESTEND)
        object Rococo : NetworkTypeUI(R.drawable.ic_polkadot_24, ROCOCO)
    }
}
