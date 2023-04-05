package com.unfinished.dsnp_wallet_kotlin.runtime

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.Gson
import com.unfinished.feature_account.domain.model.planksFromAmount
import io.novafoundation.nova.common.data.mappers.mapCryptoTypeToEncryption
import io.novafoundation.nova.common.data.network.runtime.binding.bindAccountInfo
import io.novafoundation.nova.common.data.network.runtime.calls.*
import io.novafoundation.nova.common.data.network.runtime.model.SignedBlock
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.common.utils.deriveSeed32
import io.novafoundation.nova.common.utils.orZero
import io.novafoundation.nova.common.utils.system
import io.novafoundation.nova.core.model.CryptoType
import io.novafoundation.nova.runtime.ext.accountIdOf
import io.novafoundation.nova.runtime.ext.hexAccountIdOf
import io.novafoundation.nova.runtime.ext.utilityAsset
import io.novafoundation.nova.runtime.extrinsic.CustomSignedExtensions
import io.novafoundation.nova.runtime.multiNetwork.chain.model.Chain
import io.novafoundation.nova.runtime.multiNetwork.getRuntime
import io.novafoundation.nova.runtime.multiNetwork.getSocket
import jp.co.soramitsu.fearless_utils.encrypt.EncryptionType
import jp.co.soramitsu.fearless_utils.encrypt.MultiChainEncryption
import jp.co.soramitsu.fearless_utils.encrypt.junction.SubstrateJunctionDecoder
import jp.co.soramitsu.fearless_utils.encrypt.keypair.Keypair
import jp.co.soramitsu.fearless_utils.encrypt.keypair.substrate.SubstrateKeypairFactory
import jp.co.soramitsu.fearless_utils.encrypt.seed.substrate.SubstrateSeedFactory
import jp.co.soramitsu.fearless_utils.extensions.fromHex
import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.generics.Era
import jp.co.soramitsu.fearless_utils.runtime.extrinsic.ExtrinsicBuilder
import jp.co.soramitsu.fearless_utils.runtime.extrinsic.signer.KeyPairSigner
import jp.co.soramitsu.fearless_utils.runtime.metadata.GetMetadataRequest
import jp.co.soramitsu.fearless_utils.runtime.metadata.storage
import jp.co.soramitsu.fearless_utils.runtime.metadata.storageKey
import jp.co.soramitsu.fearless_utils.wsrpc.executeAsync
import jp.co.soramitsu.fearless_utils.wsrpc.mappers.ResponseMapper
import jp.co.soramitsu.fearless_utils.wsrpc.mappers.nonNull
import jp.co.soramitsu.fearless_utils.wsrpc.mappers.pojo
import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.RuntimeRequest
import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.author.SubmitAndWatchExtrinsicRequest
import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.chain.RuntimeVersion
import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.chain.RuntimeVersionRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.junit.Before

@RunWith(AndroidJUnit4::class)
class RuntimeInstrumentedTest {

    val commonApi = FeatureUtils.getCommonApi(ApplicationProvider.getApplicationContext())
    val context = ApplicationProvider.getApplicationContext<Context>()
    lateinit var runtimeFactory: TestRuntimeFactory
    lateinit var runtime: RuntimeSnapshot
    lateinit var chain: Chain
    lateinit var gson: Gson

    //Custom details about account
    lateinit var test_account_keypair: Keypair
    lateinit var test2_account_keypair: Keypair
    val genesisHash = "0xce5d4fe1b183df0434514ebc38f28f1227df817d74b48a979cf5d2cf8e86b7d7" //don't change it
    var test_account_mnemonic =
        "mouse humble two verify ocean more giant nerve slot joke food forest"
    val test2_account_mnemonic =
        "season mule race soccer kind reunion sun walk invest enhance cactus brush"
    var test_account_address = "5FJ4JD6ntR1Q1vMVz9nZ4JoABdzuXqHJ9vE6Ksr4furaHs4r"
    var test2_account_address = "5CArEgoDzh7xE3p7NapgJxAdGkPvsH8zrh6peGNAv7Czji5G"
    var test3_account_address = "5HmcekqhKQmY1GzkaBkzyrPUt88aBPLZfwvhm5o8v1VReGsF"

    @Before
    fun setup() {
        //Required Dependencies
        gson = Gson()
        runtimeFactory = TestRuntimeFactory()
        val metaData = runtimeFactory.getJsonDataFromAsset(context, "polkadot_metadata") ?: ""
        val types = runtimeFactory.getJsonDataFromAsset(context, "polkadot_types.json") ?: ""
        val chain_str = runtimeFactory.getJsonDataFromAsset(context, "polkadot_chain.json")
        chain = gson.fromJson(chain_str, Chain::class.java)
        runBlocking {
            runtime = runtimeFactory.constructRuntimeInternal(metaData, types)
        }

        // Create Keys using mnemonic phrase
        test_account_keypair = createKeypair(test_account_mnemonic)
        test2_account_keypair = createKeypair(test2_account_mnemonic)
    }


    @Test
    fun test_getNonce_by_account_address() = runBlocking {
        val account_address_for_nonce = test_account_address
        val request = NextAccountIndexRequest(account_address_for_nonce)
        val response = executeCall(request)
        println(gson.toJson(response))
        //Make test fail if the error exists
        Assert.assertTrue(response.error?.message ?: "Error", response.error == null)
    }

    @Test
    fun test_runtimeMetadata() = runBlocking {
        val request = GetMetadataRequest
        val response = executeCallWithMapper(request, pojo<String>().nonNull())
        println(gson.toJson(response))
        //Make test fail if the error exists
        Assert.assertTrue("Metadata is empty or not valid", response.isNotEmpty())
    }

    @Test
    fun test_runtimeVersion() = runBlocking {
        val request = RuntimeVersionRequest()
        val response = executeCall(request)
        println(gson.toJson(response))
        //Make test fail if the error exists
        Assert.assertTrue(response.error?.message ?: "Error", response.error == null)
    }

    @Test
    fun test_getGenesisHash() = runBlocking {
        val request = GetBlockHashRequest(0.toBigInteger())
        val response = executeCallWithMapper(request,pojo<String>().nonNull())
        println(response)
        //Make test fail if the error exists
        Assert.assertTrue("Block hash is empty",response.isNotEmpty())
    }

    /**
     * Retrieves the block with given hash
     * If hash is null, than the latest block is returned
     */
    @Test
    fun test_getBlock() = runBlocking {
        val hash: String? = null
        val request = GetBlockRequest(hash)
        val response = executeCallWithMapper(request,pojo<SignedBlock>().nonNull())
        println(gson.toJson(response))
        //Make test fail if the error exists
        Assert.assertTrue("Parent hash is null",response.block.header.parentHash?.isNotEmpty() ?: false)
        Assert.assertTrue("Block number is negative",response.block.header.number > -1)
    }

    @Test
    fun test_executeGetStorageRequest() = runBlocking(Dispatchers.Default) {
        val key = runtime.metadata.system().storage("Account")
            .storageKey(runtime, chain.hexAccountIdOf(test_account_address).fromHex())
        val request = GetStateRequest(key)
        val response = executeCall(request)
        val scale = response.result as? String
        val accountInfo = bindAccountInfo(scale!!, runtime)
        println(gson.toJson(accountInfo))
        Assert.assertTrue("Scale is empty",scale.isNotEmpty())
    }

    @Test
    fun test_createMsaId_for_account() = runBlocking {
        val accountKeypairForMSA = test_account_keypair
        val accountAddressForMSA = test_account_address
        val accountIdForMSA = chain.accountIdOf(test_account_address)

        //Rpc Call for getting nonce
        val request_nonce = NextAccountIndexRequest(accountAddressForMSA)
        val response_nonce = executeCall(request_nonce)
        val doubleResult = response_nonce.result as Double

        //Rpc Call for getting Runtime Version
        val request_runtimeVersion = RuntimeVersionRequest()
        val response_runtimeVersion = executeCallWithMapper(request_runtimeVersion,mapper = pojo<RuntimeVersion>().nonNull())

        //Extrinsic Builder
        val signer = KeyPairSigner(accountKeypairForMSA, MultiChainEncryption.Substrate(mapCryptoTypeToEncryption(CryptoType.SR25519)))
        val extrinsicBuilder = ExtrinsicBuilder(
            tip = chain.additional?.defaultTip.orZero(),
            runtime = runtime,
            nonce = doubleResult.toInt().toBigInteger(),
            runtimeVersion = response_runtimeVersion,
            genesisHash = genesisHash.fromHex(),
            blockHash = genesisHash.fromHex(),
            era = Era.Immortal,
            customSignedExtensions = CustomSignedExtensions.extensionsWithValues(runtime),
            signer = signer,
            accountId = accountIdForMSA
        )
        val extrinsic = extrinsicBuilder
            .createMsa()
            .build()

        val request = SubmitAndWatchExtrinsicRequest(extrinsic)
        val response = executeCall(request)
        println(gson.toJson(response))
        //Make test fail if the error exists
        Assert.assertTrue(response.error?.message ?: "Error", response.error == null)
    }

    @Test
    fun test_balance_transfer() = runBlocking {
        //Account detail for transaction
        val senderAccountKeyPair = test_account_keypair
        val senderAccountAddress = test_account_address
        val recieverAccountAddress = test2_account_address
        val senderAccountId = chain.accountIdOf(test_account_address)
        val recieverAccountId = chain.accountIdOf(test2_account_address)

        //Rpc Call for getting nonce
        val request_nonce = NextAccountIndexRequest(senderAccountAddress)
        val response_nonce = executeCall(request_nonce)
        val doubleResult = response_nonce.result as Double

        //Rpc Call for getting Runtime Version
        val request_runtimeVersion = RuntimeVersionRequest()
        val response_runtimeVersion = executeCallWithMapper(request_runtimeVersion,mapper = pojo<RuntimeVersion>().nonNull())

        //Extrinsic Builder
        val signer = KeyPairSigner(senderAccountKeyPair, MultiChainEncryption.Substrate(mapCryptoTypeToEncryption(CryptoType.SR25519)))
        val extrinsicBuilder = ExtrinsicBuilder(
            tip = chain.additional?.defaultTip.orZero(),
            runtime = runtime,
            nonce = doubleResult.toInt().toBigInteger(),
            runtimeVersion = response_runtimeVersion,
            genesisHash = genesisHash.fromHex(),
            blockHash = genesisHash.fromHex(),
            era = Era.Immortal,
            customSignedExtensions = CustomSignedExtensions.extensionsWithValues(runtime),
            signer = signer,
            accountId = senderAccountId
        )
        val extrinsic = extrinsicBuilder
            .transferCall(recieverAccountId, chain.utilityAsset.planksFromAmount((0.10121).toBigDecimal()))
            .build()

        val request = SubmitAndWatchExtrinsicRequest(extrinsic)
        val response = executeCall(request)
        //Make test fail if the error exists
        Assert.assertTrue(response.error?.message ?: "Error", response.error == null)
        println("Transfer Extrinsic Response:" + gson.toJson(response))
        println("After Transfer Sender Account Details:" + printAccountDetails(senderAccountAddress))
        println("After Transfer Reciever Account Details:" + printAccountDetails(recieverAccountAddress))
    }

    private fun printAccountDetails(address: String): String {
        val key = runtime.metadata.system().storage("Account").storageKey(runtime, chain.hexAccountIdOf(address).fromHex())
        val request_storage_query = GetStateRequest(key)
        val response_storage_query = executeCall(request_storage_query)
        val scale = response_storage_query.result as? String
        Assert.assertTrue("Scale is empty",scale?.isNotEmpty() ?: false)
        val accountInfo = bindAccountInfo(scale!!, runtime)
        return gson.toJson(accountInfo)
    }

    private fun createKeypair(mnemonic: String): Keypair {
        val seed = SubstrateSeedFactory.deriveSeed32(mnemonic, null).seed
        val derivationPath: String? = null
        val decodedDerivationPath = when {
            derivationPath.isNullOrEmpty() -> null
            else -> SubstrateJunctionDecoder.decode(derivationPath)
        }
        val junctions = decodedDerivationPath?.junctions.orEmpty()
        return SubstrateKeypairFactory.generate(EncryptionType.SR25519, seed, junctions)
    }

    private fun <T> executeCallWithMapper(request: RuntimeRequest, mapper: ResponseMapper<T>) = runBlocking {
        val response = commonApi.socketSingleRequestExecutor().executeRequest(request, chain.nodes.first().url,mapper)
        println(gson.toJson(response))
        response
    }

    private fun executeCall(request: RuntimeRequest) = runBlocking {
        val response = commonApi.socketSingleRequestExecutor().executeRequest(request, chain.nodes.first().url,)
        println(gson.toJson(response))
        response
    }

    /*
    @Test
    fun test_runtime_chain_exists() {
        val chain = runtimeFactory.getJsonDataFromAsset(context, "polkadot_chain.json")
        assertTrue(!chain.isNullOrBlank())
    }

    @Test
    fun test_runtime_metadata_Exists() {
        val metaData = runtimeFactory.getJsonDataFromAsset(context, "polkadot_metadata")
        assertTrue(!metaData.isNullOrBlank())
    }

    @Test
    fun test_runtime_types_Exists() {
        val types = runtimeFactory.getJsonDataFromAsset(context, "polkadot_types.json")
        assertTrue(!types.isNullOrBlank())
    }
    */
}