package com.unfinished.feature_account.domain.interfaces

import com.unfinished.data.api.model.CryptoType
import com.unfinished.data.api.model.Language
import com.unfinished.data.api.model.Node
import com.unfinished.feature_account.domain.model.Account
import com.unfinished.feature_account.domain.model.MetaAccount
import com.unfinished.feature_account.domain.model.PreferredCryptoType
import com.unfinished.runtime.multiNetwork.chain.model.ChainId
import jp.co.soramitsu.fearless_utils.encrypt.mnemonic.Mnemonic
import kotlinx.coroutines.flow.Flow

interface AccountInteractor {

    suspend fun generateMnemonic(): Mnemonic

    fun getCryptoTypes(): List<CryptoType>

    suspend fun getPreferredCryptoType(chainId: ChainId? = null): PreferredCryptoType

    suspend fun isCodeSet(): Boolean

    suspend fun savePin(code: String)

    suspend fun isPinCorrect(code: String): Boolean

    suspend fun isBiometricEnabled(): Boolean

    suspend fun setBiometricOn()

    suspend fun setBiometricOff()

    suspend fun getMetaAccount(metaId: Long): MetaAccount

    suspend fun selectMetaAccount(metaId: Long)

    suspend fun deleteAccount(metaId: Long)

    suspend fun updateMetaAccountPositions(idsInNewOrder: List<Long>)

    fun nodesFlow(): Flow<List<Node>>

    suspend fun getNode(nodeId: Int): Node

    fun getLanguages(): List<Language>

    suspend fun getSelectedLanguage(): Language

    suspend fun changeSelectedLanguage(language: Language)

    suspend fun addNode(nodeName: String, nodeHost: String): Result<Unit>

    suspend fun updateNode(nodeId: Int, newName: String, newHost: String): Result<Unit>

    suspend fun getAccountsByNetworkTypeWithSelectedNode(networkType: Node.NetworkType): Pair<List<Account>, Node>

    suspend fun selectNodeAndAccount(nodeId: Int, accountAddress: String)

    suspend fun selectNode(nodeId: Int)

    suspend fun deleteNode(nodeId: Int)

    suspend fun getChainAddress(metaId: Long, chainId: ChainId): String?
}
