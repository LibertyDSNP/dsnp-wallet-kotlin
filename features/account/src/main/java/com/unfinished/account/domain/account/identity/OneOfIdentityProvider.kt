package com.unfinished.account.domain.account.identity

import com.unfinished.data.multiNetwork.chain.model.ChainId
import jp.co.soramitsu.fearless_utils.extensions.tryFindNonNull
import jp.co.soramitsu.fearless_utils.runtime.AccountId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class OneOfIdentityProvider(
    private val delegates: List<IdentityProvider>
) : IdentityProvider {

    override suspend fun identityFor(accountId: AccountId, chainId: ChainId): Identity? = withContext(Dispatchers.IO) {
        delegates.tryFindNonNull {
            it.identityFor(accountId, chainId)
        }
    }

    override suspend fun identitiesFor(accountIds: Collection<AccountId>, chainId: ChainId): Map<com.unfinished.common.address.AccountIdKey, Identity?> = withContext(Dispatchers.IO) {
        delegates.tryFindNonNull {
            it.identitiesFor(accountIds, chainId)
        }.orEmpty()
    }
}
