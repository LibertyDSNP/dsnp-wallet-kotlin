package com.unfinished.feature_account.domain.account.export.json

import com.unfinished.feature_account.domain.interfaces.AccountRepository
import io.novafoundation.nova.runtime.multiNetwork.ChainRegistry
import io.novafoundation.nova.runtime.multiNetwork.chain.model.ChainId

class ExportJsonInteractor(
    private val accountRepository: AccountRepository,
    private val chainRegistry: ChainRegistry,
) {

    suspend fun generateRestoreJson(
        metaId: Long,
        chainId: ChainId,
        password: String,
    ): Result<String> {
        val metaAccount = accountRepository.getMetaAccount(metaId)
        val chain = chainRegistry.getChain(chainId)

        return runCatching {
            accountRepository.generateRestoreJson(metaAccount, chain, password)
        }
    }
}
