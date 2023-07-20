package com.unfinished.data.usecase.rpc

import com.unfinished.data.model.account.MetaAccount
import com.unfinished.data.multiNetwork.ChainRegistry
import com.unfinished.data.multiNetwork.chain.model.Chain
import com.unfinished.data.multiNetwork.getRuntime
import com.unfinished.data.multiNetwork.rpc.RpcCalls
import com.unfinished.data.multiNetwork.runtime.binding.AccountInfo
import com.unfinished.data.multiNetwork.runtime.binding.bindAccountInfo
import com.unfinished.data.util.ext.hexAccountIdOf
import com.unfinished.data.util.ext.system
import jp.co.soramitsu.fearless_utils.extensions.fromHex
import jp.co.soramitsu.fearless_utils.runtime.metadata.storage
import jp.co.soramitsu.fearless_utils.runtime.metadata.storageKey
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountUseCase @Inject constructor(
    private val rpcCalls: RpcCalls,
    private val chainRegistry: ChainRegistry
){

    suspend fun getAccountInfo (chain: Chain, metaAccount: MetaAccount) = callbackFlow {
        val runtime = chainRegistry.getRuntime(chain.id)
        val key = runtime.metadata.system().storage("Account")
            .storageKey(runtime, metaAccount.substrateAccountId)
        val scale = rpcCalls.getStateAccount(chain.id, key).result as? String
        scale?.let {
            trySend(bindAccountInfo(it, runtime))
        } ?: run {
            trySend(null)
        }
        awaitClose()
    }
}