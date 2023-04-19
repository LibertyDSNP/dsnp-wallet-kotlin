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
import com.unfinished.feature_account.domain.model.MetaAccount
import com.unfinished.feature_account.domain.model.planksFromAmount
import com.unfinished.feature_account.domain.model.toPlanks
import com.unfinished.feature_account.presentation.importing.source.model.ImportError
import io.novafoundation.nova.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.novafoundation.nova.common.R
import io.novafoundation.nova.common.data.network.runtime.binding.BlockHash
import io.novafoundation.nova.common.data.network.runtime.binding.bindAccountInfo
import io.novafoundation.nova.common.data.network.runtime.calls.*
import io.novafoundation.nova.common.data.network.runtime.model.SignedBlock
import io.novafoundation.nova.common.data.secrets.v2.ChainAccountSecrets
import io.novafoundation.nova.common.data.secrets.v2.MetaAccountSecrets
import io.novafoundation.nova.common.resources.ResourceManager
import io.novafoundation.nova.common.utils.*
import io.novafoundation.nova.core_db.model.chain.ChainNodeLocal
import io.novafoundation.nova.runtime.ext.*
import io.novafoundation.nova.runtime.extrinsic.ExtrinsicBuilderFactory
import io.novafoundation.nova.runtime.extrinsic.ExtrinsicStatus
import io.novafoundation.nova.runtime.extrinsic.asExtrinsicStatus
import io.novafoundation.nova.runtime.multiNetwork.ChainRegistry
import io.novafoundation.nova.runtime.multiNetwork.chain.*
import io.novafoundation.nova.runtime.multiNetwork.chain.model.Chain
import io.novafoundation.nova.runtime.multiNetwork.connection.ChainConnection
import io.novafoundation.nova.runtime.multiNetwork.connection.ChainConnectionFactory
import io.novafoundation.nova.runtime.multiNetwork.connection.ConnectionPool
import io.novafoundation.nova.runtime.multiNetwork.getRuntime
import io.novafoundation.nova.runtime.multiNetwork.getSocket
import io.novafoundation.nova.runtime.multiNetwork.runtime.repository.EventsRepository
import io.novafoundation.nova.runtime.network.rpc.RpcCalls
import jp.co.soramitsu.fearless_utils.encrypt.EncryptionType
import jp.co.soramitsu.fearless_utils.encrypt.junction.BIP32JunctionDecoder
import jp.co.soramitsu.fearless_utils.encrypt.junction.JunctionDecoder
import jp.co.soramitsu.fearless_utils.exceptions.Bip39Exception
import jp.co.soramitsu.fearless_utils.extensions.fromHex
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.generics.MultiSignature
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.generics.prepareForEncoding
import jp.co.soramitsu.fearless_utils.runtime.metadata.storage
import jp.co.soramitsu.fearless_utils.runtime.metadata.storageKey
import jp.co.soramitsu.fearless_utils.scale.EncodableStruct
import jp.co.soramitsu.fearless_utils.wsrpc.SocketService
import jp.co.soramitsu.fearless_utils.wsrpc.executeAsync
import jp.co.soramitsu.fearless_utils.wsrpc.mappers.nonNull
import jp.co.soramitsu.fearless_utils.wsrpc.mappers.pojo
import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.RuntimeRequest
import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.author.SubmitAndWatchExtrinsicRequest
import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.chain.RuntimeVersion
import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.chain.RuntimeVersionRequest
import jp.co.soramitsu.fearless_utils.wsrpc.response.RpcResponse
import jp.co.soramitsu.fearless_utils.wsrpc.subscription.response.SubscriptionChange
import jp.co.soramitsu.fearless_utils.wsrpc.subscriptionFlow
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.math.BigDecimal
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
    private val gson: Gson
) : BaseViewModel() {

    val chainId = "496e2f8a93bf4576317f998d01480f9068014b368856ec088a63e57071cd1d49"
    //    val mnemonic2 = "season mule race soccer kind reunion sun walk invest enhance cactus brush"
    //  var mnemonic = "mouse humble two verify ocean more giant nerve slot joke food forest"
    var gensisHash: String? = null
    var accountAddres = "5CXVZbHLoDprVw2r2tWLzgNJqZZdu8rZ5dBSPiSXgSH8V3fp"
    var accountAddres2 = "5CV1EtqhSyompvpNnZ2pWvcFp2rycoRVFUxAHyAuVMvb9SnG"
    var accountAddresForMsa = "5GNQNJaWFUXQdHSYSKYQGvkzoDbt11xNgHq2xra23Noxpxyn"

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

    fun getGesisHashFromBlockHash() = runBlocking {
        val request_gensisHash = rpcCalls.getBlockHash(chainId,0.toBigInteger())
        gensisHash = request_gensisHash
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
        val scale = chainRegistry.getSocket(chain.id).executeAsync(GetStateRequest(key)).result as? String
        scale?.let {
            val accountInfo = bindAccountInfo(it, runtime)
            accountInfo
        }
    }

    suspend fun getRuntimeVersion(chain: Chain): RuntimeVersion {
        val request = RuntimeVersionRequest()
        return chainRegistry.getSocket(chain.id).executeAsync(request, mapper = pojo<RuntimeVersion>().nonNull())
    }

    /**
     * Retrieves the block with given hash
     * If hash is null, than the latest block is returned
     */
    suspend fun getBlock(chain: Chain, hash: String? = null): SignedBlock {
        val blockRequest = GetBlockRequest(hash)

        return chainRegistry.getSocket(chain.id).executeAsync(blockRequest, mapper = pojo<SignedBlock>().nonNull())
    }

    suspend fun getGenesisHash(chain: Chain): String {
        val blockRequest = GetBlockHashRequest(0.toBigInteger())
        return chainRegistry.getSocket(chain.id).executeAsync(blockRequest, mapper = pojo<String>().nonNull())
    }

    suspend fun getEvents(chain: Chain) {
        viewModelScope.launch {
            val currentAccount = getCurrentAccount()
            val runtime = chainRegistry.getRuntime(chain.id)
            val key = runtime.metadata.system().storage("Events").storageKey(runtime)
            val scale = chainRegistry.getSocket(chain.id).executeAsync(GetStateRequest(key)).result
            Log.e("test",scale.toString())
        }
    }

    suspend fun executeEventBlock(chain: Chain){
        viewModelScope.launch {
            val result = eventsRepository.getEventsInBlockForFrequency(chain.id,"0xf05f654bfe0d31dab180d5b07869f14b496dc9da6275fc6a9f2787bdab4678d8")
            result.onSuccess {
                Log.e("TEST",it.second.name)
            }.onFailure {
                Log.e("TEST",it.message,it)
            }
        }
    }

    suspend fun checkExtrinsicStatus(chain: Chain,blockHash: String) = eventsRepository.getEventsInBlockForFrequency(chain.id,blockHash)

    suspend fun executeAnyExtrinsic(chain: Chain) {
        val metaAccount = accountRepository.findMetaAccount(chain.accountIdOf(accountAddresForMsa))
        val signer = signerProvider.signerFor(metaAccount!!)
        val accountId = chain.accountIdOf(accountAddresForMsa)
        val extrinsic = extrinsicBuilderFactory.create(chain, signer, accountId)
            .createMsa()
            .build()
        val extrinsicStatus = rpcCalls.submitAndWatchExtrinsic(chain.id,extrinsic)
            .filterIsInstance<ExtrinsicStatus.Finalized>()
            .first()
       Log.e("test",extrinsicStatus.blockHash)

    }

    suspend fun testTransfer(chain: Chain, enteredAmount: Float) = flow {
        val result = kotlin.runCatching {
            val metaAccount = accountRepository.findMetaAccount(chain.accountIdOf(accountAddres))
            val signer = signerProvider.signerFor(metaAccount!!)
            val accountId = chain.accountIdOf(accountAddres)
            val extrinsic = extrinsicBuilderFactory.create(chain, signer, accountId)
                .transferCall(
                    destAccount = chain.accountIdOf(accountAddres2),
                    amount = enteredAmount.toPlanks()
                ).build()
            val extrinsicStatus = rpcCalls.submitAndWatchExtrinsic(chain.id,extrinsic)
                .filterIsInstance<ExtrinsicStatus.Finalized>()
                .first()
            val status = checkExtrinsicStatus(chain,extrinsicStatus.blockHash)
            Pair(extrinsicStatus.blockHash,status)
        }
        emit(result)
    }.flowOn(Dispatchers.IO)

    suspend fun createMsa(chain: Chain) = flow {
        val result = kotlin.runCatching {
            val metaAccount = accountRepository.findMetaAccount(chain.accountIdOf(accountAddresForMsa))
            val signer = signerProvider.signerFor(metaAccount!!)
            val accountId = chain.accountIdOf(accountAddresForMsa)
            val extrinsic = extrinsicBuilderFactory.create(chain, signer, accountId)
                .createMsa()
                .build()
            val extrinsicStatus = rpcCalls.submitAndWatchExtrinsic(chain.id,extrinsic)
                .filterIsInstance<ExtrinsicStatus.Finalized>()
                .first()
            val status = checkExtrinsicStatus(chain,extrinsicStatus.blockHash)
            Pair(extrinsicStatus.blockHash,status)
        }
        emit(result)
    }.flowOn(Dispatchers.IO)

    suspend fun addKeyToMsa(chain: Chain) = flow {
        val metaAccount = accountRepository.findMetaAccount(chain.accountIdOf(accountAddres))
        val signer = signerProvider.signerFor(metaAccount!!)
        val accountId = chain.accountIdOf(accountAddres)
        val extrinsic = extrinsicBuilderFactory.create(chain, signer, accountId)
            .addKeyToMsa(
                key = accountAddres,
                proof = "",
                addKeyPayload = AddKeyPayload(
                    msaId = 2,
                    nonce = 0.toBigInteger()
                )
            )
            .build()
        val extrinsicStatus = rpcCalls.submitAndWatchExtrinsic(chain.id,extrinsic)
            .filterIsInstance<ExtrinsicStatus.Finalized>()
            .first()
        checkExtrinsicStatus(chain,extrinsicStatus.blockHash)
        emit(extrinsicStatus.blockHash)
    }.flowOn(Dispatchers.IO)

}


