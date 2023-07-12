package com.unfinished.feature_account.domain.account.advancedEncryption

import com.unfinished.data.mappers.mapEncryptionToCryptoType
import com.unfinished.data.secrets.v2.SecretStoreV2
import com.unfinished.data.secrets.v2.derivationPath
import com.unfinished.data.secrets.v2.ethereumDerivationPath
import com.unfinished.data.secrets.v2.substrateDerivationPath
import com.unfinished.data.util.DEFAULT_DERIVATION_PATH
import com.unfinished.data.util.fold
import com.unfinished.common.utils.input.Input
import com.unfinished.common.utils.input.disabledInput
import com.unfinished.common.utils.input.modifiableInput
import com.unfinished.common.utils.input.unmodifiableInput
import com.unfinished.data.util.nullIfEmpty
import com.unfinished.data.api.model.CryptoType
import com.unfinished.feature_account.data.secrets.getAccountSecrets
import com.unfinished.feature_account.domain.interfaces.AccountRepository
import com.unfinished.feature_account.domain.model.chainAccountFor
import com.unfinished.runtime.multiNetwork.ChainRegistry
import com.unfinished.runtime.multiNetwork.chain.model.ChainId
import jp.co.soramitsu.fearless_utils.encrypt.EncryptionType
import jp.co.soramitsu.fearless_utils.encrypt.MultiChainEncryption
import jp.co.soramitsu.fearless_utils.encrypt.junction.BIP32JunctionDecoder

private val DEFAULT_SUBSTRATE_ENCRYPTION = mapEncryptionToCryptoType(EncryptionType.SR25519)
private val ETHEREUM_ENCRYPTION = mapEncryptionToCryptoType(MultiChainEncryption.Ethereum.encryptionType)

private const val DEFAULT_SUBSTRATE_DERIVATION_PATH = ""
private val ETHEREUM_DEFAULT_DERIVATION_PATH = BIP32JunctionDecoder.DEFAULT_DERIVATION_PATH

private typealias DerivationPathModifier = String?.() -> Input<String>

class AdvancedEncryptionInteractor(
    private val accountRepository: AccountRepository,
    private val secretStoreV2: SecretStoreV2,
    private val chainRegistry: ChainRegistry,
) {

    fun getCryptoTypes(): List<CryptoType> {
        return accountRepository.getEncryptionTypes()
    }

    private suspend fun getViewInitialInputState(
        metaAccountId: Long,
        chainId: ChainId,
        hideDerivationPaths: Boolean
    ): AdvancedEncryptionInput {
        val chain = chainRegistry.getChain(chainId)
        val metaAccount = accountRepository.getMetaAccount(metaAccountId)

        val accountSecrets = secretStoreV2.getAccountSecrets(metaAccount, chain)

        val derivationPathModifier = derivationPathModifier(hideDerivationPaths)

        return accountSecrets.fold(
            left = { metaAccountSecrets ->
                if (chain.isEthereumBased) {
                    AdvancedEncryptionInput(
                        substrateCryptoType = disabledInput(),
                        substrateDerivationPath = disabledInput(),
                        ethereumCryptoType = ETHEREUM_ENCRYPTION.asReadOnlyInput(),
                        ethereumDerivationPath = metaAccountSecrets.ethereumDerivationPath.derivationPathModifier()
                    )
                } else {
                    AdvancedEncryptionInput(
                        substrateCryptoType = metaAccount.substrateCryptoType.asReadOnlyInput(),
                        substrateDerivationPath = metaAccountSecrets.substrateDerivationPath.derivationPathModifier(),
                        ethereumCryptoType = disabledInput(),
                        ethereumDerivationPath = disabledInput()
                    )
                }
            },
            right = { chainAccountSecrets ->
                if (chain.isEthereumBased) {
                    AdvancedEncryptionInput(
                        substrateCryptoType = disabledInput(),
                        substrateDerivationPath = disabledInput(),
                        ethereumCryptoType = ETHEREUM_ENCRYPTION.asReadOnlyInput(),
                        ethereumDerivationPath = chainAccountSecrets.derivationPath.derivationPathModifier()
                    )
                } else {
                    val chainAccount = metaAccount.chainAccountFor(chainId)

                    AdvancedEncryptionInput(
                        substrateCryptoType = chainAccount.cryptoType.asReadOnlyInput(),
                        substrateDerivationPath = chainAccountSecrets.derivationPath.derivationPathModifier(),
                        ethereumCryptoType = disabledInput(),
                        ethereumDerivationPath = disabledInput()
                    )
                }
            }
        )
    }

    private suspend fun getChangeInitialInputState(chainId: ChainId?) = if (chainId != null) {
        val chain = chainRegistry.getChain(chainId)

        if (chain.isEthereumBased) { // Ethereum Chain Account
            AdvancedEncryptionInput(
                substrateCryptoType = disabledInput(),
                substrateDerivationPath = disabledInput(),
                ethereumCryptoType = ETHEREUM_ENCRYPTION.unmodifiableInput(),
                ethereumDerivationPath = ETHEREUM_DEFAULT_DERIVATION_PATH.modifiableInput()
            )
        } else { // Substrate Chain Account
            AdvancedEncryptionInput(
                substrateCryptoType = DEFAULT_SUBSTRATE_ENCRYPTION.modifiableInput(),
                substrateDerivationPath = DEFAULT_SUBSTRATE_DERIVATION_PATH.modifiableInput(),
                ethereumCryptoType = disabledInput(),
                ethereumDerivationPath = disabledInput()
            )
        }
    } else { // MetaAccount
        AdvancedEncryptionInput(
            substrateCryptoType = DEFAULT_SUBSTRATE_ENCRYPTION.modifiableInput(),
            substrateDerivationPath = DEFAULT_SUBSTRATE_DERIVATION_PATH.modifiableInput(),
            ethereumCryptoType = ETHEREUM_ENCRYPTION.unmodifiableInput(),
            ethereumDerivationPath = ETHEREUM_DEFAULT_DERIVATION_PATH.modifiableInput()
        )
    }

    private fun <T> Input<T>.hideIf(condition: Boolean) = if (condition) {
        Input.Disabled
    } else {
        this
    }

    private fun derivationPathModifier(hideDerivationPaths: Boolean): DerivationPathModifier = {
        asReadOnlyInput().hideIf(hideDerivationPaths)
    }

    private fun <T> T?.asReadOnlyInput() = this?.unmodifiableInput() ?: disabledInput()

    private fun String?.asReadOnlyStringInput() = this?.nullIfEmpty().asReadOnlyInput()
}
