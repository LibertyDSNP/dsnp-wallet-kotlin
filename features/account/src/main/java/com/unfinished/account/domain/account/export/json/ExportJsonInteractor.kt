package com.unfinished.account.domain.account.export.json

import com.unfinished.account.domain.interfaces.AccountRepository
import com.unfinished.runtime.multiNetwork.ChainRegistry
import com.unfinished.runtime.multiNetwork.chain.model.ChainId

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
