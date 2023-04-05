package com.unfinished.feature_account

import io.novafoundation.nova.common.utils.default
import io.novafoundation.nova.runtime.ext.*
import io.novafoundation.nova.runtime.multiNetwork.chain.model.Chain
import io.novafoundation.nova.runtime.multiNetwork.runtime.*
import io.novafoundation.nova.test_shared.whenever
import jp.co.soramitsu.fearless_utils.encrypt.EncryptionType
import jp.co.soramitsu.fearless_utils.encrypt.junction.BIP32JunctionDecoder
import jp.co.soramitsu.fearless_utils.encrypt.keypair.Keypair
import jp.co.soramitsu.fearless_utils.encrypt.keypair.substrate.SubstrateKeypairFactory
import jp.co.soramitsu.fearless_utils.encrypt.seed.substrate.SubstrateSeedFactory

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class ExtrinsicsUnitTest {

    val chainId = "496e2f8a93bf4576317f998d01480f9068014b368856ec088a63e57071cd1d49"
    val genesisHash = "0xce5d4fe1b183df0434514ebc38f28f1227df817d74b48a979cf5d2cf8e86b7d7"
    val chainAssests = listOf(
        Chain.Asset(
            symbol = "TEST",
            precision = 10,
            name = "Test",
            priceId = "test",
            staking = Chain.Asset.StakingType.ALEPH_ZERO,
            type = Chain.Asset.Type.Native,
            buyProviders = emptyMap(),
            iconUrl = null,
            id = 0,
            chainId = chainId
        )
    )

    var aliceMnemonic = "mouse humble two verify ocean more giant nerve slot joke food forest"
    val bobMnemonic = "season mule race soccer kind reunion sun walk invest enhance cactus brush"

    var aliceAccountAddres = "5FJ4JD6ntR1Q1vMVz9nZ4JoABdzuXqHJ9vE6Ksr4furaHs4r"
    var bobAccountAddres = "5CArEgoDzh7xE3p7NapgJxAdGkPvsH8zrh6peGNAv7Czji5G"
    var accountAddresForMsa = "5FJ4JD6ntR1Q1vMVz9nZ4JoABdzuXqHJ9vE6Ksr4furaHs4r"

    val aliceKeyPair = createKeypair(aliceMnemonic)
    val bobKeyPair = createKeypair(bobMnemonic)

    @Mock
    lateinit var chain: Chain

    private fun createKeypair(mnemonic: String): Keypair {
        val seed = SubstrateSeedFactory.deriveSeed(mnemonic, password = null).seed
        val junctions = BIP32JunctionDecoder.default().junctions
        return SubstrateKeypairFactory.generate(EncryptionType.SR25519,seed, junctions)
    }

    @Before
    fun setup() {
        whenever(chain.id).thenReturn(chainId)
        whenever(chain.assets).thenReturn(chainAssests)
    }


}