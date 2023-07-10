package com.unfinished.feature_account.domain.model


import com.unfinished.common.core.api.model.CryptoType
import com.unfinished.common.core.api.model.Network
import jp.co.soramitsu.fearless_utils.extensions.fromHex


data class Account(
    val address: String,
    val name: String?,
    val accountIdHex: String,
    val cryptoType: CryptoType, // TODO make optional
    val position: Int,
    val network: Network, // TODO remove when account management will be rewritten,
) {

    val accountId = accountIdHex.fromHex()
}
