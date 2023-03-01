package com.unfinished.feature_account.presentation.test

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.google.gson.JsonObject
import com.unfinished.feature_account.data.extrinsic.ExtrinsicService
import com.unfinished.feature_account.data.secrets.AccountSecretsFactory
import com.unfinished.feature_account.data.signer.SignerProvider
import com.unfinished.feature_account.domain.account.advancedEncryption.AdvancedEncryption
import com.unfinished.feature_account.domain.interfaces.AccountAlreadyExistsException
import com.unfinished.feature_account.domain.interfaces.AccountDataSource
import com.unfinished.feature_account.domain.interfaces.AccountRepository
import com.unfinished.feature_account.domain.model.AddAccountType
import com.unfinished.feature_account.domain.model.planksFromAmount
import io.novafoundation.nova.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.novafoundation.nova.common.data.network.runtime.binding.bindAccountId
import io.novafoundation.nova.common.data.network.runtime.binding.bindAccountInfo
import io.novafoundation.nova.common.data.network.runtime.calls.GetBlockHashRequest
import io.novafoundation.nova.common.data.network.runtime.calls.GetBlockRequest
import io.novafoundation.nova.common.data.network.runtime.calls.GetStateRequest
import io.novafoundation.nova.common.data.network.runtime.calls.transferCall
import io.novafoundation.nova.common.data.network.runtime.model.SignedBlock
import io.novafoundation.nova.common.data.secrets.v2.ChainAccountSecrets
import io.novafoundation.nova.common.data.secrets.v2.MetaAccountSecrets
import io.novafoundation.nova.common.utils.balances
import io.novafoundation.nova.common.utils.default
import io.novafoundation.nova.common.utils.system
import io.novafoundation.nova.runtime.ext.accountIdOf
import io.novafoundation.nova.runtime.ext.hexAccountIdOf
import io.novafoundation.nova.runtime.ext.utilityAsset
import io.novafoundation.nova.runtime.extrinsic.ExtrinsicBuilderFactory
import io.novafoundation.nova.runtime.multiNetwork.ChainRegistry
import io.novafoundation.nova.runtime.multiNetwork.chain.model.Chain
import io.novafoundation.nova.runtime.multiNetwork.connection.ChainConnection
import io.novafoundation.nova.runtime.multiNetwork.getRuntime
import io.novafoundation.nova.runtime.multiNetwork.getSocket
import io.novafoundation.nova.runtime.network.rpc.RpcCalls
import io.novafoundation.nova.runtime.storage.source.RemoteStorageSource
import jp.co.soramitsu.fearless_utils.encrypt.MultiChainEncryption
import jp.co.soramitsu.fearless_utils.encrypt.junction.BIP32JunctionDecoder
import jp.co.soramitsu.fearless_utils.encrypt.keypair.ethereum.EthereumKeypairFactory
import jp.co.soramitsu.fearless_utils.encrypt.seed.ethereum.EthereumSeedFactory
import jp.co.soramitsu.fearless_utils.extensions.fromHex
import jp.co.soramitsu.fearless_utils.extensions.toHexString
import jp.co.soramitsu.fearless_utils.runtime.extrinsic.signer.KeyPairSigner
import jp.co.soramitsu.fearless_utils.runtime.metadata.call
import jp.co.soramitsu.fearless_utils.runtime.metadata.storage
import jp.co.soramitsu.fearless_utils.runtime.metadata.storageKey
import jp.co.soramitsu.fearless_utils.scale.EncodableStruct
import jp.co.soramitsu.fearless_utils.wsrpc.executeAsync
import jp.co.soramitsu.fearless_utils.wsrpc.mappers.nonNull
import jp.co.soramitsu.fearless_utils.wsrpc.mappers.pojo
import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.chain.RuntimeVersion
import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.chain.RuntimeVersionRequest
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import org.json.JSONObject
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
) : BaseViewModel() {

    val mnemonic2 = "season mule race soccer kind reunion sun walk invest enhance cactus brush"
    val mnemonic = "mouse humble two verify ocean more giant nerve slot joke food forest"
    val chainId = "496e2f8a93bf4576317f998d01480f9068014b368856ec088a63e57071cd1d49"
    val accountAddres2 = "5CArEgoDzh7xE3p7NapgJxAdGkPvsH8zrh6peGNAv7Czji5G"
    val accountAddres = "5FJ4JD6ntR1Q1vMVz9nZ4JoABdzuXqHJ9vE6Ksr4furaHs4r"

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

    fun executeGetStorageRequest(chain: Chain) = runBlocking(Dispatchers.Default) {
        val currentAccount = getCurrentAccount()
        val runtime = chainRegistry.getRuntime(chain.id)
        val key = runtime.metadata.system().storage("Account")
            .storageKey(runtime, currentAccount?.fromHex())
        val scale =
            chainRegistry.getSocket(chain.id).executeAsync(GetStateRequest(key)).result as? String
        scale?.let {
            val accountInfo = bindAccountInfo(it, runtime)
            val freeBalance = accountInfo.data.free
            println("GetStateRequest -> Free balance: $freeBalance")
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

    suspend fun testTransfer(chain: Chain) = withContext(Dispatchers.IO) {
        val metaAccount = accountRepository.findMetaAccount(chain.accountIdOf(accountAddres))
        val signer = signerProvider.signerFor(metaAccount!!)
        val accountId = chain.accountIdOf(accountAddres)
        val extrinsic = extrinsicBuilderFactory.create(chain, signer, accountId)
            .transferCall(
                destAccount = chain.accountIdOf(accountAddres2),
                amount = chain.utilityAsset.planksFromAmount(0.1111.toBigDecimal())
            ).build()
        val hash = rpcCalls.submitExtrinsic(chain.id, extrinsic)
        print(hash)
    }

}


