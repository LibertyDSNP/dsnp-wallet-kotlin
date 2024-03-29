package com.unfinished.account.domain.account.identity

import com.unfinished.common.address.AccountIdKey
import com.unfinished.runtime.multiNetwork.chain.model.ChainId
import jp.co.soramitsu.fearless_utils.runtime.AccountId

interface IdentityProvider {

    companion object;

    /**
     * Returns, if present, an identity for the given [accountId] inside specified [chainId]
     */
    suspend fun identityFor(accountId: AccountId, chainId: ChainId): Identity?

    /**
     * Bulk version of [identityFor]. Default implementation is unoptimized and just performs N single requests to [identityFor].
     */
    suspend fun identitiesFor(accountIds: Collection<AccountId>, chainId: ChainId): Map<AccountIdKey, Identity?> {
        return accountIds.associateBy(
            keySelector = ::AccountIdKey,
            valueTransform = { identityFor(it, chainId) }
        )
    }
}

fun IdentityProvider.Companion.oneOf(vararg delegates: IdentityProvider): IdentityProvider {
    return OneOfIdentityProvider(delegates.toList())
}
