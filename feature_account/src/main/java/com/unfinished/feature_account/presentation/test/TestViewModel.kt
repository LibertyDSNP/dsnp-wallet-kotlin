package com.unfinished.feature_account.presentation.test

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
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
import com.unfinished.feature_account.presentation.importing.source.model.ImportError
import io.novafoundation.nova.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.novafoundation.nova.common.R
import io.novafoundation.nova.common.data.network.runtime.binding.bindAccountInfo
import io.novafoundation.nova.common.data.network.runtime.calls.*
import io.novafoundation.nova.common.data.network.runtime.model.SignedBlock
import io.novafoundation.nova.common.data.secrets.v2.ChainAccountSecrets
import io.novafoundation.nova.common.data.secrets.v2.MetaAccountSecrets
import io.novafoundation.nova.common.resources.ResourceManager
import io.novafoundation.nova.common.utils.system
import io.novafoundation.nova.runtime.ext.accountIdOf
import io.novafoundation.nova.runtime.ext.addressOf
import io.novafoundation.nova.runtime.ext.hexAccountIdOf
import io.novafoundation.nova.runtime.ext.utilityAsset
import io.novafoundation.nova.runtime.extrinsic.ExtrinsicBuilderFactory
import io.novafoundation.nova.runtime.extrinsic.asExtrinsicStatus
import io.novafoundation.nova.runtime.multiNetwork.ChainRegistry
import io.novafoundation.nova.runtime.multiNetwork.chain.model.Chain
import io.novafoundation.nova.runtime.multiNetwork.getRuntime
import io.novafoundation.nova.runtime.multiNetwork.getSocket
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
import jp.co.soramitsu.fearless_utils.wsrpc.executeAsync
import jp.co.soramitsu.fearless_utils.wsrpc.mappers.nonNull
import jp.co.soramitsu.fearless_utils.wsrpc.mappers.pojo
import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.chain.RuntimeVersion
import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.chain.RuntimeVersionRequest
import jp.co.soramitsu.fearless_utils.wsrpc.response.RpcResponse
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
    private val resourceManager: ResourceManager
) : BaseViewModel() {

    val chainId = "496e2f8a93bf4576317f998d01480f9068014b368856ec088a63e57071cd1d49"
//    val mnemonic2 = "season mule race soccer kind reunion sun walk invest enhance cactus brush"
//    var mnemonic = "mouse humble two verify ocean more giant nerve slot joke food forest"
    var accountAddres2 = "5CArEgoDzh7xE3p7NapgJxAdGkPvsH8zrh6peGNAv7Czji5G"
    var accountAddres = "5FJ4JD6ntR1Q1vMVz9nZ4JoABdzuXqHJ9vE6Ksr4furaHs4r"
    var accountAddresForMsa = "5FJ4JD6ntR1Q1vMVz9nZ4JoABdzuXqHJ9vE6Ksr4furaHs4r"

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
            getScretes(derivationPaths,addAccountType,accountSource)
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
        accountAddres = account.substrateAccountId?.let { getChain()?.addressOf(it) } ?: ""
    }

    fun setSelectedTransferAccount(account: MetaAccount) {
        accountAddres2 = account.substrateAccountId?.let { getChain()?.addressOf(it) } ?: ""
    }

    fun setSelectedAccountForMsa(account: MetaAccount) {
        accountAddresForMsa = account.substrateAccountId?.let { getChain()?.addressOf(it) } ?: ""
    }

    fun getCurrentAccount() = runBlocking {
        val chains = chainRegistry.currentChains.first()
        val chain = chains.find { it.id == chainId }
        chain?.hexAccountIdOf(accountAddres)
    }

    fun getChain() = runBlocking {
        val chains = chainRegistry.currentChains.first()
        val chain = chains.find { it.id == chainId }
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

    suspend fun getStateRuntimeVersion(chain: Chain): Pair<Int, Int> {
        val request = RuntimeVersionRequest()
        val result = chainRegistry.getSocket(chain.id)
            .executeAsync(request, mapper = pojo<RuntimeVersion>().nonNull())
        val specVersion = result.specVersion
        val transactionVersion = result.transactionVersion
        return Pair(specVersion, transactionVersion)
    }

    suspend fun executeAnyExtrinsic(chain: Chain) {
        extrinsicService.submitExtrinsicWithAnySuitableWallet(
            chain = chain,
            accountId = chain.accountIdOf(accountAddres),
            formExtrinsic = {
                transferCall(
                    destAccount = chain.accountIdOf(accountAddres2),
                    amount = 100000.toBigInteger()
                )
            }
        )
    }

    suspend fun testTransfer(chain: Chain,enteredAmount: Float) = flow {
        val metaAccount = accountRepository.findMetaAccount(chain.accountIdOf(accountAddres))
        val signer = signerProvider.signerFor(metaAccount!!)
        val accountId = chain.accountIdOf(accountAddres)
        val extrinsic = extrinsicBuilderFactory.create(chain, signer, accountId)
            .transferCall(
                destAccount = chain.accountIdOf(accountAddres2),
                amount = chain.utilityAsset.planksFromAmount(enteredAmount.toBigDecimal())
            ).build()
        val hash = rpcCalls.submitExtrinsic(chain.id, extrinsic)
        emit(hash)
    }.flowOn(Dispatchers.IO)

    suspend fun createMsa(chain: Chain) = flow {
        val metaAccount = accountRepository.findMetaAccount(chain.accountIdOf(accountAddresForMsa))
        val signer = signerProvider.signerFor(metaAccount!!)
        val accountId = chain.accountIdOf(accountAddresForMsa)
        val extrinsic = extrinsicBuilderFactory.create(chain, signer, accountId)
            .createMsa()
            .build()
        val hash = rpcCalls.submitExtrinsic(chain.id, extrinsic)
        emit(hash)
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
        val hash = rpcCalls.submitExtrinsic(chain.id, extrinsic)
        emit(hash)
    }.flowOn(Dispatchers.IO)

}


