package com.unfinished.feature_account.presentation.test

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.unfinished.feature_account.data.extrinsic.ExtrinsicService
import com.unfinished.feature_account.data.secrets.AccountSecretsFactory
import com.unfinished.feature_account.data.signer.SignerProvider
import com.unfinished.feature_account.domain.account.advancedEncryption.AdvancedEncryption
import com.unfinished.feature_account.domain.interfaces.AccountAlreadyExistsException
import com.unfinished.feature_account.domain.interfaces.AccountDataSource
import com.unfinished.feature_account.domain.interfaces.AccountRepository
import com.unfinished.feature_account.domain.model.AddAccountType
import com.unfinished.feature_account.domain.model.LightMetaAccount
import com.unfinished.feature_account.domain.model.MetaAccount
import com.unfinished.feature_account.domain.model.toPlanks
import com.unfinished.feature_account.presentation.importing.source.model.ImportError
import dagger.hilt.android.lifecycle.HiltViewModel
import io.novafoundation.nova.common.R
import io.novafoundation.nova.common.base.BaseViewModel
import io.novafoundation.nova.common.data.network.runtime.binding.AccountInfo
import io.novafoundation.nova.common.data.network.runtime.binding.bindAccountInfo
import io.novafoundation.nova.common.data.network.runtime.binding.checkIfExtrinsicFailed
import io.novafoundation.nova.common.data.network.runtime.calls.*
import io.novafoundation.nova.common.data.network.runtime.model.SignedBlock
import io.novafoundation.nova.common.data.network.runtime.model.event.*
import io.novafoundation.nova.common.data.secrets.v2.ChainAccountSecrets
import io.novafoundation.nova.common.data.secrets.v2.KeyPairSchema
import io.novafoundation.nova.common.data.secrets.v2.MetaAccountSecrets
import io.novafoundation.nova.common.data.secrets.v2.SecretStoreV2
import io.novafoundation.nova.common.data.secrets.v2.getMetaAccountKeypair
import io.novafoundation.nova.common.resources.ResourceManager
import io.novafoundation.nova.common.utils.*
import io.novafoundation.nova.core.model.CryptoType
import io.novafoundation.nova.runtime.ext.*
import io.novafoundation.nova.runtime.extrinsic.CustomSignedExtensions
import io.novafoundation.nova.runtime.extrinsic.ExtrinsicBuilderFactory
import io.novafoundation.nova.runtime.extrinsic.ExtrinsicStatus
import io.novafoundation.nova.runtime.multiNetwork.ChainRegistry
import io.novafoundation.nova.runtime.multiNetwork.chain.model.Chain
import io.novafoundation.nova.runtime.multiNetwork.connection.ChainConnectionFactory
import io.novafoundation.nova.runtime.multiNetwork.connection.ConnectionPool
import io.novafoundation.nova.runtime.multiNetwork.getChainOrNull
import io.novafoundation.nova.runtime.multiNetwork.getRuntime
import io.novafoundation.nova.runtime.multiNetwork.getSocket
import io.novafoundation.nova.runtime.multiNetwork.runtime.repository.EventsRepository
import io.novafoundation.nova.runtime.network.rpc.RpcCalls
import io.novafoundation.nova.runtime.sign.DAppParsedExtrinsic
import io.novafoundation.nova.runtime.sign.SignerPayload
import jp.co.soramitsu.fearless_utils.encrypt.EncryptionType
import jp.co.soramitsu.fearless_utils.encrypt.MultiChainEncryption

import jp.co.soramitsu.fearless_utils.encrypt.Sr25519
import jp.co.soramitsu.fearless_utils.encrypt.junction.BIP32JunctionDecoder
import jp.co.soramitsu.fearless_utils.encrypt.junction.JunctionDecoder
import jp.co.soramitsu.fearless_utils.encrypt.keypair.substrate.Sr25519Keypair
import jp.co.soramitsu.fearless_utils.encrypt.mnemonic.Mnemonic
import jp.co.soramitsu.fearless_utils.encrypt.mnemonic.MnemonicCreator
import jp.co.soramitsu.fearless_utils.exceptions.Bip39Exception
import jp.co.soramitsu.fearless_utils.extensions.asEthereumPublicKey
import jp.co.soramitsu.fearless_utils.extensions.fromHex
import jp.co.soramitsu.fearless_utils.extensions.toAccountId
import jp.co.soramitsu.fearless_utils.extensions.toHexString
import jp.co.soramitsu.fearless_utils.runtime.AccountId
import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.composite.DictEnum
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.composite.Struct
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.fromHex
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.generics.EraType
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.generics.Extrinsic
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.generics.GenericCall
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.generics.MultiSignature
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.generics.prepareForEncoding
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.toByteArray
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.toHex
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.toHexUntyped
import jp.co.soramitsu.fearless_utils.runtime.extrinsic.ExtrinsicBuilder
import jp.co.soramitsu.fearless_utils.runtime.extrinsic.signer.Signer
import jp.co.soramitsu.fearless_utils.runtime.extrinsic.signer.SignerPayloadExtrinsic
import jp.co.soramitsu.fearless_utils.runtime.extrinsic.signer.SignerPayloadRaw
import jp.co.soramitsu.fearless_utils.runtime.extrinsic.signer.encodedSignaturePayload
import jp.co.soramitsu.fearless_utils.runtime.extrinsic.signer.fromHex
import jp.co.soramitsu.fearless_utils.runtime.metadata.storage
import jp.co.soramitsu.fearless_utils.runtime.metadata.storageKey
import jp.co.soramitsu.fearless_utils.scale.EncodableStruct
import jp.co.soramitsu.fearless_utils.scale.Schema
import jp.co.soramitsu.fearless_utils.scale.byteArray
import jp.co.soramitsu.fearless_utils.scale.dataType.DataType
import jp.co.soramitsu.fearless_utils.scale.dataType.string
import jp.co.soramitsu.fearless_utils.scale.dataType.toHex
import jp.co.soramitsu.fearless_utils.ss58.SS58Encoder.toAccountId
import jp.co.soramitsu.fearless_utils.wsrpc.executeAsync
import jp.co.soramitsu.fearless_utils.wsrpc.mappers.nonNull
import jp.co.soramitsu.fearless_utils.wsrpc.mappers.pojo
import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.chain.RuntimeVersion
import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.chain.RuntimeVersionRequest
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.junit.Assert
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.Exception
import java.math.BigInteger
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(
    private val accountSecretsFactory: AccountSecretsFactory,
    private val chainRegistry: ChainRegistry,
    private val extrinsicService: ExtrinsicService,
    private val accountDataSource: AccountDataSource,
    private val rpcCalls: RpcCalls,
    private val extrinsicBuilderFactory: ExtrinsicBuilderFactory,
    private val accountRepository: AccountRepository,
    private val signerProvider: SignerProvider,
    private val resourceManager: ResourceManager,
    private val chainConnectionFactory: ChainConnectionFactory,
    private val eventsRepository: EventsRepository,
    private val gson: Gson,
    private val secreteStoreV2: SecretStoreV2
) : BaseViewModel() {

    val chainId = "496e2f8a93bf4576317f998d01480f9068014b368856ec088a63e57071cd1d49"

    //    val mnemonic2 = "season mule race soccer kind reunion sun walk invest enhance cactus brush"
    //  var mnemonic = "mouse humble two verify ocean more giant nerve slot joke food forest"
    var gensisHash: String? = null
    var accountAddres = "5CXVZbHLoDprVw2r2tWLzgNJqZZdu8rZ5dBSPiSXgSH8V3fp"
    var accountAddres2 = "5CV1EtqhSyompvpNnZ2pWvcFp2rycoRVFUxAHyAuVMvb9SnG"
    var accountAddresForMsa = "5GNQNJaWFUXQdHSYSKYQGvkzoDbt11xNgHq2xra23Noxpxyn"
    var accountAddresForBalance = "5GNQNJaWFUXQdHSYSKYQGvkzoDbt11xNgHq2xra23Noxpxyn"

    init {
        getGesisHashFromBlockHash()
    }

    fun setUpNewConnection(chainUrl: String) = runBlocking {
        getChain()?.let {
            if (chainUrl.isNotEmpty()) {
                it.nodes = it.nodes.map {
                    it.url = chainUrl
                    it
                }
            }
            val connectionPool = ConnectionPool(chainConnectionFactory)
            connectionPool.removeConnection(it.id)
            connectionPool.setupConnection(it)
        }
    }

    fun getGesisHashFromBlockHash() {
        viewModelScope.launch {
            val request_gensisHash = rpcCalls.getBlockHash(chainId, 0.toBigInteger())
            gensisHash = request_gensisHash
        }
    }

    private fun handleCreateAccountError(throwable: Throwable) {
        var errorMessage = handleError(throwable)

        if (errorMessage == null) {
            errorMessage = when (throwable) {
                is AccountAlreadyExistsException -> ImportError(
                    titleRes = R.string.account_add_already_exists_message,
                    messageRes = R.string.account_error_try_another_one
                )

                is JunctionDecoder.DecodingError, is BIP32JunctionDecoder.DecodingError -> ImportError(
                    titleRes = R.string.account_invalid_derivation_path_title,
                    messageRes = R.string.account_invalid_derivation_path_message_v2_2_0
                )

                else -> ImportError()
            }
        }

        val title = resourceManager.getString(errorMessage.titleRes)
        val message = resourceManager.getString(errorMessage.messageRes)

        showError(title, message)
    }

    fun handleError(throwable: Throwable): ImportError? {
        return when (throwable) {
            is Bip39Exception -> ImportError(
                titleRes = R.string.import_mnemonic_invalid_title,
                messageRes = R.string.mnemonic_error_try_another_one_v2_2_0
            )

            else -> null
        }
    }

    suspend fun createAccount(
        derivationPaths: AdvancedEncryption.DerivationPaths,
        addAccountType: AddAccountType,
        accountSource: AccountSecretsFactory.AccountSource,
        onResultCallback: (Triple<Boolean, EncodableStruct<MetaAccountSecrets>?, EncodableStruct<ChainAccountSecrets>?>) -> Unit
    ) {
        kotlin.runCatching {
            getScretes(derivationPaths, addAccountType, accountSource)
        }.onSuccess {
            onResultCallback.invoke(it)
        }.onFailure {
            handleCreateAccountError(it)
        }
    }

    suspend fun getScretes(
        derivationPaths: AdvancedEncryption.DerivationPaths,
        addAccountType: AddAccountType,
        accountSource: AccountSecretsFactory.AccountSource
    ): Triple<Boolean, EncodableStruct<MetaAccountSecrets>?, EncodableStruct<ChainAccountSecrets>?> {
        return when (addAccountType) {
            is AddAccountType.MetaAccount -> {
                val (secrets, substrateCryptoType) = accountSecretsFactory.metaAccountSecrets(
                    substrateDerivationPath = derivationPaths.substrate,
                    ethereumDerivationPath = derivationPaths.ethereum,
                    accountSource = accountSource
                )
                transformingInsertionErrors {
                    accountDataSource.insertMetaAccountFromSecrets(
                        name = addAccountType.name,
                        substrateCryptoType = substrateCryptoType,
                        secrets = secrets
                    )
                }
                Triple(true, secrets, null)
            }

            is AddAccountType.ChainAccount -> {
                val chain = chainRegistry.getChain(addAccountType.chainId)

                val derivationPath =
                    if (chain.isEthereumBased) derivationPaths.ethereum else derivationPaths.substrate

                val (secrets, cryptoType) = accountSecretsFactory.chainAccountSecrets(
                    derivationPath = derivationPath,
                    accountSource = accountSource,
                    isEthereum = chain.isEthereumBased
                )
                transformingInsertionErrors {
                    accountDataSource.insertChainAccount(
                        metaId = addAccountType.metaId,
                        chain = chain,
                        cryptoType = cryptoType,
                        secrets = secrets
                    )
                }
                Triple(false, null, secrets)
            }
        }
    }

    private inline fun <R> transformingInsertionErrors(action: () -> R) = try {
        action()
    } catch (_: SQLiteConstraintException) {
        throw AccountAlreadyExistsException()
    }

    suspend fun getMetaAccountFromMnemonic(
        chain: Chain,
        name: String,
        mnemonic: String = MnemonicCreator.randomMnemonic(Mnemonic.Length.TWELVE).words
    ) = withContext(Dispatchers.IO) {
        val (secrets, substrateCryptoType) = accountSecretsFactory.metaAccountSecrets(
            substrateDerivationPath = "",
            ethereumDerivationPath = "//44//60//0/0/0",
            accountSource = AccountSecretsFactory.AccountSource.Mnemonic(
                CryptoType.SR25519,
                mnemonic
            )
        )
        val substratePublicKey =
            secrets[MetaAccountSecrets.SubstrateKeypair][KeyPairSchema.PublicKey]
        val ethereumPublicKey =
            secrets[MetaAccountSecrets.EthereumKeypair]?.get(KeyPairSchema.PublicKey)
        val metaAccountLocal = MetaAccount(
            substratePublicKey = substratePublicKey,
            substrateCryptoType = substrateCryptoType,
            substrateAccountId = substratePublicKey.substrateAccountId(),
            ethereumPublicKey = ethereumPublicKey,
            ethereumAddress = ethereumPublicKey?.asEthereumPublicKey()?.toAccountId()?.value,
            name = name,
            isSelected = false,
            id = 2,
            chainAccounts = mapOf(
                Pair(
                    chain.id, MetaAccount.ChainAccount(
                        metaId = 2,
                        chain = chain,
                        publicKey = substratePublicKey,
                        accountId = substratePublicKey.substrateAccountId(),
                        cryptoType = CryptoType.SR25519
                    )
                )
            ),
            type = LightMetaAccount.Type.SECRETS
        )
        metaAccountLocal
    }

    fun getMetaAccounts() = runBlocking {
        accountRepository.allMetaAccounts()
    }

    fun setSelectedAccount(account: MetaAccount) {
        accountAddres = account.substratePublicKey?.let { getChain()?.addressOf(it) } ?: ""
    }

    fun setSelectedTransferAccount(account: MetaAccount) {
        accountAddres2 = account.substratePublicKey?.let { getChain()?.addressOf(it) } ?: ""
    }

    fun setSelectedAccountForMsa(account: MetaAccount) {
        accountAddresForMsa = account.substratePublicKey?.let { getChain()?.addressOf(it) } ?: ""
    }

    fun setSelectedAccountForBalance(account: MetaAccount) {
        accountAddresForBalance =
            account.substratePublicKey?.let { getChain()?.addressOf(it) } ?: ""
    }

    fun getCurrentAccount() = runBlocking {
        val chains = chainRegistry.currentChains.first()
        val chain = chains.find { it.id == chainId }
        chain?.hexAccountIdOf(accountAddres)
    }

    fun getChain() = runBlocking {
        val chains = chainRegistry.currentChains.first()
        val chain = chains.find { it.id == chainId }
        gensisHash?.let { chain?.ghash = it }
        chain
    }

    fun getMetaData(chain: Chain) = runBlocking {
        chainRegistry.getRuntime(chain.id)
    }

    fun executeGetStorageRequest(chain: Chain) = runBlocking(Dispatchers.Default) {
        val currentAccount = getCurrentAccount()
        val runtime = chainRegistry.getRuntime(chain.id)
        val key = runtime.metadata.system().storage("Account")
            .storageKey(runtime, currentAccount?.fromHex())
        val scale =
            chainRegistry.getSocket(chain.id).executeAsync(GetStateRequest(key)).result as? String
        scale?.let {
            val accountInfo = bindAccountInfo(it, runtime)
            accountInfo
        }
    }

    suspend fun getRuntimeVersion(chain: Chain): RuntimeVersion {
        val request = RuntimeVersionRequest()
        return chainRegistry.getSocket(chain.id)
            .executeAsync(request, mapper = pojo<RuntimeVersion>().nonNull())
    }

    /**
     * Retrieves the block with given hash
     * If hash is null, than the latest block is returned
     */
    suspend fun getBlock(chain: Chain, hash: String? = null): SignedBlock {
        val blockRequest = GetBlockRequest(hash)

        return chainRegistry.getSocket(chain.id)
            .executeAsync(blockRequest, mapper = pojo<SignedBlock>().nonNull())
    }

    suspend fun getGenesisHash(chain: Chain): String {
        val blockRequest = GetBlockHashRequest(0.toBigInteger())
        return chainRegistry.getSocket(chain.id)
            .executeAsync(blockRequest, mapper = pojo<String>().nonNull())
    }

    suspend fun getEvents(chain: Chain) {
        viewModelScope.launch {
            val currentAccount = getCurrentAccount()
            val runtime = chainRegistry.getRuntime(chain.id)
            val key = runtime.metadata.system().storage("Events").storageKey(runtime)
            val scale = chainRegistry.getSocket(chain.id).executeAsync(GetStateRequest(key)).result
            Log.e("test", scale.toString())
        }
    }

    suspend fun executeEventBlock(chain: Chain) {
        viewModelScope.launch {
            val result = eventsRepository.getEventsInBlockForFrequency(
                chainId = chain.id,
                blockHash = "0xbb566de0c47801ccad062e7def2632759c22ee3540df58b7c0eb4f7c7dfac5e8"
            )
            result.onSuccess {
                it.forEach { event ->
                    when (event) {
                        is EventType.System -> {
                            Log.e("EventType", "EventType.System\n" + gson.toJson(event))
                        }

                        is EventType.Balance -> {
                            Log.e("EventType", "EventType.Balance\n" + gson.toJson(event))
                        }

                        is EventType.MsaEvent -> {
                            Log.e("EventType", "EventType.MsaEvent\n" + gson.toJson(event))
                        }

                        is EventType.TransactionPayment -> {
                            Log.e(
                                "EventType",
                                "EventType.TransactionPayment\n" + gson.toJson(event)
                            )
                        }

                        else -> EventType.Incompaitable
                    }
                }
            }.onFailure {
                Log.e("TEST", it.message, it)
            }
        }
    }

    suspend fun getBlockEvents(chain: Chain, blockHash: String) =
        eventsRepository.getEventsInBlockForFrequency(chain.id, blockHash)

    suspend fun executeAnyExtrinsic(chain: Chain) {
        val metaAccount = accountRepository.findMetaAccount(chain.accountIdOf(accountAddresForMsa))
        val signer = signerProvider.signerFor(metaAccount!!)
        val accountId = chain.accountIdOf(accountAddresForMsa)
        val extrinsic = extrinsicBuilderFactory.create(chain, signer, accountId)
            .createMsa()
            .build()
        val extrinsicStatus = rpcCalls.submitAndWatchExtrinsic(chain.id, extrinsic)
            .filterIsInstance<ExtrinsicStatus.Finalized>()
            .first()
        Log.e("test", extrinsicStatus.blockHash)

    }

    suspend fun checkAccountDetails(chain: Chain): AccountInfo? {
        val runtime = chainRegistry.getRuntime(chain.id)
        val key = runtime.metadata.system().storage("Account")
            .storageKey(runtime, chain.hexAccountIdOf(accountAddresForBalance).fromHex())
        val scale = rpcCalls.getStateAccount(chain.id, key).result as? String ?: return null
        val accountInfo = bindAccountInfo(scale, runtime)
        return accountInfo
    }

    suspend fun testTransfer(chain: Chain, enteredAmount: Float) = flow {
        kotlin.runCatching {
            val metaAccount = accountRepository.findMetaAccount(chain.accountIdOf(accountAddres))
            val signer = signerProvider.signerFor(metaAccount!!)
            val accountId = chain.accountIdOf(accountAddres)
            val extrinsic = extrinsicBuilderFactory.create(chain, signer, accountId)
                .transferCall(
                    destAccount = chain.accountIdOf(accountAddres2),
                    amount = enteredAmount.toPlanks()
                ).build()
            val extrinsicStatus = rpcCalls.submitAndWatchExtrinsic(chain.id, extrinsic)
                .filterIsInstance<ExtrinsicStatus.Finalized>()
                .first()
            val events = getBlockEvents(chain, extrinsicStatus.blockHash)
            events.onSuccess {
                it.checkIfExtrinsicFailed()?.let {
                    emit(Pair(extrinsicStatus.blockHash, it.error?.second?.name))
                } ?: kotlin.run {
                    emit(Pair(extrinsicStatus.blockHash, null))
                }
            }.onFailure {
                emit(Pair(extrinsicStatus.blockHash, it.message ?: "Error"))
            }
        }
    }.flowOn(Dispatchers.IO)

    suspend fun createMsa(chain: Chain) = flow {
        kotlin.runCatching {
            val metaAccount =
                accountRepository.findMetaAccount(chain.accountIdOf(accountAddresForMsa))
            val signer = signerProvider.signerFor(metaAccount!!)
            val accountId = chain.accountIdOf(accountAddresForMsa)
            val extrinsic = extrinsicBuilderFactory.create(chain, signer, accountId)
                .createMsa()
                .build()
            val extrinsicStatus = rpcCalls.submitAndWatchExtrinsic(chain.id, extrinsic)
            val finalized =
                extrinsicStatus.filterIsInstance<ExtrinsicStatus.Finalized>().firstOrNull()
            finalized?.apply {
                val events = getBlockEvents(chain, finalized.blockHash)
                events.onSuccess {
                    it.checkIfExtrinsicFailed()?.let {
                        emit(Triple(finalized.blockHash, null, it.error?.second?.name))
                    } ?: kotlin.run {
                        val msaEvent = it.map { it as? EventType.MsaEvent }.filterNotNull()
                        emit(Triple(finalized.blockHash, msaEvent.firstOrNull(), null))
                    }
                }.onFailure {
                    emit(Triple(finalized.blockHash, null, it.message ?: "Error"))
                }
            }
        }.onFailure { exception ->
            emit(Triple(null, null, exception.message ?: "Error"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun addKeyToMsa(
        chain: Chain,
        msaId: BigInteger,
        expiration: BigInteger,
        msaOwnerMetaAccount: MetaAccount,
        newKeyOwnerMetaAccount: MetaAccount
    ) = flow {
        kotlin.runCatching {
            val msaOwnerAccountId = msaOwnerMetaAccount.substrateAccountId
                ?: throw Exception("Account Id can't be null")
            val newKeyOwnerAccountId = newKeyOwnerMetaAccount.substrateAccountId ?: throw Exception(
                "Account Id can't be null"
            )
            val msaOwnerProof = signerProvider.signerFor(msaOwnerMetaAccount).run {
                val payload = generateAddMsaKeyPayload(
                    msaId = msaId,
                    expiration = expiration,
                    accountId = newKeyOwnerAccountId,
                    chain = chain
                ).fromHex()
                generateSignatureProof(payload, msaOwnerAccountId)
            }
            val newKeyOwnerProof = signerProvider.signerFor(newKeyOwnerMetaAccount).run {
                val payload = generateAddMsaKeyPayload(
                    msaId = msaId,
                    expiration = expiration,
                    accountId = newKeyOwnerAccountId,
                    chain = chain
                ).fromHex()
                generateSignatureProof(payload, newKeyOwnerAccountId)
            }
            val extrinsic = extrinsicBuilderFactory.create(
                chain,
                signerProvider.signerFor(msaOwnerMetaAccount),
                msaOwnerAccountId
            )
                .addPublicKeyToMsa(
                    msaOwnerPublicKey = msaOwnerAccountId,
                    msaOwnerProof = msaOwnerProof,
                    newKeyOwnerProof = newKeyOwnerProof,
                    addKeyPayload = Struct.Instance(
                        mapOf(
                            "msaId" to msaId,
                            "expiration" to expiration,
                            "newPublicKey" to newKeyOwnerAccountId
                        )
                    )
                ).build()
            val extrinsicStatus = rpcCalls.submitAndWatchExtrinsic(chain.id, extrinsic)
                .filterIsInstance<ExtrinsicStatus.Finalized>()
                .first()
            val events = getBlockEvents(chain, extrinsicStatus.blockHash)
            events.onSuccess {
                it.checkIfExtrinsicFailed()?.let {
                    emit(Pair(extrinsicStatus.blockHash, it.error?.second?.name))
                } ?: kotlin.run {
                    emit(Pair(extrinsicStatus.blockHash, null))
                }
            }.onFailure {
                emit(Pair(extrinsicStatus.blockHash, it.message ?: "Error"))
            }
        }.onFailure {
            Log.e("failed", it.message ?: "Error")
        }

    }.flowOn(Dispatchers.IO)

    suspend fun generateAddMsaKeyPayload(
        msaId: BigInteger,
        expiration: BigInteger,
        accountId: AccountId,
        chain: Chain
    ): String {
        val runtime = chainRegistry.getRuntime(chain.id)
        val addKeyData = runtime.typeRegistry.get("pallet_msa.types.AddKeyData")
        val prefix = "0x3c42797465733e" // <Bytes> -> Hex string
        val postfix = "3c2f42797465733e" // </Bytes> -> Hex string
        val payload = addKeyData!!.toHexUntyped(
            runtime, Struct.Instance(
                mapOf(
                    "msaId" to msaId,
                    "expiration" to expiration,
                    "newPublicKey" to accountId
                )
            )
        ).substring(2) //Exclude 0x
        return prefix + payload + postfix
    }

    suspend fun Signer.generateSignatre(
        payload: ByteArray,
        accountId: AccountId
    ): ByteArray {
        return signRaw(SignerPayloadRaw(payload, accountId)).signature
    }

    suspend fun Signer.generateSignatureProof(
        payload: ByteArray,
        accountId: AccountId
    ): DictEnum.Entry<*> {
        val signature = signRaw(SignerPayloadRaw(payload, accountId)).signature
        return MultiSignature(
            EncryptionType.SR25519,
            signature
        ).prepareForEncoding() as DictEnum.Entry<*>
    }
}




