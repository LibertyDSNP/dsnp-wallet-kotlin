package com.unfinished.account.data.repository.datasource

import com.unfinished.data.secrets.v1.SecretStoreV1
import com.unfinished.data.secrets.v2.ChainAccountSecrets
import com.unfinished.data.secrets.v2.KeyPairSchema
import com.unfinished.data.secrets.v2.MetaAccountSecrets
import com.unfinished.data.secrets.v2.SecretStoreV2
import com.unfinished.data.storage.Preferences
import com.unfinished.data.storage.encrypt.EncryptedPreferences
import com.unfinished.common.utils.inBackground
import com.unfinished.runtime.util.substrateAccountId
import com.unfinished.data.model.CryptoType
import com.unfinished.data.model.Language
import com.unfinished.data.model.Node
import com.unfinished.data.db.dao.MetaAccountDao
import com.unfinished.data.db.dao.NodeDao
import com.unfinished.data.db.model.chain.ChainAccountLocal
import com.unfinished.data.db.model.chain.MetaAccountLocal
import com.unfinished.data.db.model.chain.MetaAccountPositionUpdate
import com.unfinished.account.domain.model.Account
import com.unfinished.account.domain.model.AuthType
import com.unfinished.account.domain.model.MetaAccount
import com.unfinished.account.domain.model.MetaAccountOrdering
import com.unfinished.account.data.mappers.mapChainAccountToAccount
import com.unfinished.account.data.mappers.mapMetaAccountLocalToMetaAccount
import com.unfinished.account.data.mappers.mapMetaAccountToAccount
import com.unfinished.account.data.mappers.mapNodeLocalToNode
import com.unfinished.account.data.repository.datasource.migration.AccountDataMigration
import com.unfinished.account.domain.interfaces.AccountDataSource
import com.unfinished.runtime.util.accountIdOf
import com.unfinished.runtime.multiNetwork.ChainRegistry
import com.unfinished.runtime.multiNetwork.chain.model.Chain
import jp.co.soramitsu.fearless_utils.extensions.asEthereumPublicKey
import jp.co.soramitsu.fearless_utils.extensions.toAccountId
import jp.co.soramitsu.fearless_utils.runtime.AccountId
import jp.co.soramitsu.fearless_utils.scale.EncodableStruct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val PREFS_AUTH_TYPE = "auth_type"
private const val PREFS_PIN_CODE = "pin_code"

class AccountDataSourceImpl(
    private val preferences: Preferences,
    private val encryptedPreferences: EncryptedPreferences,
    private val nodeDao: NodeDao,
    private val metaAccountDao: MetaAccountDao,
    private val chainRegistry: ChainRegistry,
    private val secretStoreV2: SecretStoreV2,
    secretStoreV1: SecretStoreV1,
    accountDataMigration: AccountDataMigration,
) : AccountDataSource, SecretStoreV1 by secretStoreV1 {

    init {
        migrateIfNeeded(accountDataMigration)
    }

    private fun migrateIfNeeded(migration: AccountDataMigration) = async {
        if (migration.migrationNeeded()) {
            migration.migrate(::saveSecuritySource)
        }
    }

    private val selectedMetaAccountLocal = metaAccountDao.selectedMetaAccountInfoFlow()
        .shareIn(GlobalScope, started = SharingStarted.Eagerly, replay = 1)

    private val selectedMetaAccountFlow = combine(
        chainRegistry.chainsById,
        selectedMetaAccountLocal.filterNotNull(),
        ::mapMetaAccountLocalToMetaAccount
    )
        .inBackground()
        .shareIn(GlobalScope, started = SharingStarted.Eagerly, replay = 1)

    private val selectedNodeFlow = nodeDao.activeNodeFlow()
        .map { it?.let(::mapNodeLocalToNode) }
        .shareIn(GlobalScope, started = SharingStarted.Eagerly, replay = 1)

    /**
     * Fast lookup table for accessing account based on accountId
     */
    override val selectedAccountMapping = selectedMetaAccountFlow.map { metaAccount ->
        val mapping = metaAccount.chainAccounts.mapValuesTo(mutableMapOf<String, Account?>()) { (_, chainAccount) ->
            mapChainAccountToAccount(metaAccount, chainAccount)
        }

        val chains = chainRegistry.chainsById.first()

        chains.forEach { (chainId, chain) ->
            if (chainId !in mapping) {
                mapping[chainId] = mapMetaAccountToAccount(chain, metaAccount)
            }
        }

        mapping
    }
        .inBackground()
        .shareIn(GlobalScope, started = SharingStarted.Eagerly, replay = 1)

    override suspend fun saveAuthType(authType: AuthType) = withContext(Dispatchers.IO) {
        preferences.putString(PREFS_AUTH_TYPE, authType.toString())
    }

    override suspend fun getAuthType(): AuthType = withContext(Dispatchers.IO) {
        val savedValue = preferences.getString(PREFS_AUTH_TYPE)

        if (savedValue == null) {
            AuthType.PINCODE
        } else {
            AuthType.valueOf(savedValue)
        }
    }

    override suspend fun savePinCode(pinCode: String) = withContext(Dispatchers.IO) {
        encryptedPreferences.putEncryptedString(PREFS_PIN_CODE, pinCode)
    }

    override suspend fun getPinCode(): String? {
        return withContext(Dispatchers.IO) {
            encryptedPreferences.getDecryptedString(PREFS_PIN_CODE)
        }
    }

    override suspend fun saveSelectedNode(node: Node) = withContext(Dispatchers.Default) {
        nodeDao.switchActiveNode(node.id)
    }

    override suspend fun getSelectedNode(): Node? = selectedNodeFlow.first()

    override suspend fun anyAccountSelected(): Boolean = selectedMetaAccountLocal.first() != null

    override suspend fun saveSelectedAccount(account: Account) = withContext(Dispatchers.Default) {
        // TODO remove compatibility stub
    }

    override fun selectedNodeFlow(): Flow<Node> {
        return selectedNodeFlow
            .filterNotNull()
    }

    override suspend fun getSelectedMetaAccount(): MetaAccount {
        return selectedMetaAccountFlow.first()
    }

    override fun selectedMetaAccountFlow(): Flow<MetaAccount> = selectedMetaAccountFlow

    override suspend fun findMetaAccount(accountId: ByteArray): MetaAccount? {
        return metaAccountDao.getMetaAccountInfo(accountId)?.let {
            mapMetaAccountLocalToMetaAccount(chainRegistry.chainsById.first(), it)
        }
    }

    override suspend fun accountNameFor(accountId: AccountId): String? {
        return metaAccountDao.metaAccountNameFor(accountId)
    }

    override suspend fun allMetaAccounts(): List<MetaAccount> {
        val chainsById = chainRegistry.chainsById.first()

        return metaAccountDao.getJoinedMetaAccountsInfo().map {
            mapMetaAccountLocalToMetaAccount(chainsById, it)
        }
    }

    override fun allMetaAccountsFlow(): Flow<List<MetaAccount>> {
        return metaAccountDao.getJoinedMetaAccountsInfoFlow().map { accountsLocal ->
            val chainsById = chainRegistry.chainsById.first()

            accountsLocal.map {
                mapMetaAccountLocalToMetaAccount(chainsById, it)
            }
        }
    }
    override suspend fun selectMetaAccount(metaId: Long) {
        metaAccountDao.selectMetaAccount(metaId)
    }

    override suspend fun updateAccountPositions(accountOrdering: List<MetaAccountOrdering>) = withContext(Dispatchers.Default) {
        val positionUpdates = accountOrdering.map {
            MetaAccountPositionUpdate(id = it.id, position = it.position)
        }

        metaAccountDao.updatePositions(positionUpdates)
    }

    override suspend fun getSelectedLanguage(): Language = withContext(Dispatchers.IO) {
        preferences.getCurrentLanguage() ?: throw IllegalArgumentException("No language selected")
    }

    override suspend fun changeSelectedLanguage(language: Language) = withContext(Dispatchers.IO) {
        preferences.saveCurrentLanguage(language.iso)
    }

    override suspend fun accountExists(accountId: AccountId): Boolean {
        return metaAccountDao.isMetaAccountExists(accountId)
    }

    override suspend fun getMetaAccount(metaId: Long): MetaAccount {
        val joinedMetaAccountInfo = metaAccountDao.getJoinedMetaAccountInfo(metaId)

        return mapMetaAccountLocalToMetaAccount(chainRegistry.chainsById.first(), joinedMetaAccountInfo)
    }

    override suspend fun updateMetaAccountName(metaId: Long, newName: String) {
        metaAccountDao.updateName(metaId, newName)
    }

    override suspend fun deleteMetaAccount(metaId: Long) {
        val joinedMetaAccountInfo = metaAccountDao.getJoinedMetaAccountInfo(metaId)
        val chainAccountIds = joinedMetaAccountInfo.chainAccounts.map(ChainAccountLocal::accountId)

        metaAccountDao.delete(metaId)
        secretStoreV2.clearSecrets(metaId, chainAccountIds)
    }

    override suspend fun insertMetaAccountFromSecrets(
        name: String,
        substrateCryptoType: CryptoType,
        secrets: EncodableStruct<MetaAccountSecrets>
    ) = withContext(Dispatchers.Default) {
        val substratePublicKey = secrets[MetaAccountSecrets.SubstrateKeypair][KeyPairSchema.PublicKey]
        val ethereumPublicKey = secrets[MetaAccountSecrets.EthereumKeypair]?.get(KeyPairSchema.PublicKey)

        val metaAccountLocal = MetaAccountLocal(
            substratePublicKey = substratePublicKey,
            substrateCryptoType = substrateCryptoType,
            substrateAccountId = substratePublicKey.substrateAccountId(),
            ethereumPublicKey = ethereumPublicKey,
            ethereumAddress = ethereumPublicKey?.asEthereumPublicKey()?.toAccountId()?.value,
            name = name,
            isSelected = false,
            position = metaAccountDao.nextAccountPosition(),
            type = MetaAccountLocal.Type.SECRETS
        )

        val metaId = metaAccountDao.insertMetaAccount(metaAccountLocal)
        secretStoreV2.putMetaAccountSecrets(metaId, secrets)

        metaId
    }

    override suspend fun insertChainAccount(
        metaId: Long,
        chain: Chain,
        cryptoType: CryptoType,
        secrets: EncodableStruct<ChainAccountSecrets>
    ) = withContext(Dispatchers.Default) {
        val publicKey = secrets[ChainAccountSecrets.Keypair][KeyPairSchema.PublicKey]
        val accountId = chain.accountIdOf(publicKey)

        val chainAccountLocal = ChainAccountLocal(
            metaId = metaId,
            chainId = chain.id,
            publicKey = publicKey,
            accountId = accountId,
            cryptoType = cryptoType
        )

        metaAccountDao.insertChainAccount(chainAccountLocal)
        secretStoreV2.putChainAccountSecrets(metaId, accountId, secrets)
    }

    private inline fun async(crossinline action: suspend () -> Unit) {
        GlobalScope.launch(Dispatchers.Default) {
            action()
        }
    }
}
