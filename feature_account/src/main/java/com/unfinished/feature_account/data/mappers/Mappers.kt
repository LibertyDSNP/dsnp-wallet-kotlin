package com.unfinished.feature_account.data.mappers

import io.novafoundation.nova.common.R
import com.unfinished.feature_account.domain.model.*
import com.unfinished.feature_account.presentation.mixin.AccountNameChooserMixin
import com.unfinished.feature_account.presentation.model.account.add.AddAccountPayload
import com.unfinished.feature_account.presentation.model.advance.encryption.CryptoTypeModel
import com.unfinished.feature_account.presentation.model.advance.network.NetworkModel
import com.unfinished.feature_account.presentation.model.advance.network.NodeModel
import io.novafoundation.nova.common.resources.ResourceManager
import io.novafoundation.nova.common.utils.filterNotNull
import io.novafoundation.nova.core.model.CryptoType
import io.novafoundation.nova.core.model.Node
import io.novafoundation.nova.core.model.Node.NetworkType
import io.novafoundation.nova.core_db.dao.MetaAccountWithBalanceLocal
import io.novafoundation.nova.core_db.model.NodeLocal
import io.novafoundation.nova.core_db.model.chain.ChainAccountLocal
import io.novafoundation.nova.core_db.model.chain.JoinedMetaAccountInfo
import io.novafoundation.nova.core_db.model.chain.MetaAccountLocal
import io.novafoundation.nova.runtime.ext.addressOf
import io.novafoundation.nova.runtime.ext.hexAccountIdOf
import io.novafoundation.nova.runtime.multiNetwork.chain.model.Chain
import io.novafoundation.nova.runtime.multiNetwork.chain.model.ChainId
import jp.co.soramitsu.fearless_utils.extensions.toHexString

fun mapNetworkTypeToNetworkModel(networkType: NetworkType): NetworkModel {
    val type = when (networkType) {
        NetworkType.KUSAMA -> NetworkModel.NetworkTypeUI.Kusama
        NetworkType.POLKADOT -> NetworkModel.NetworkTypeUI.Polkadot
        NetworkType.WESTEND -> NetworkModel.NetworkTypeUI.Westend
        NetworkType.ROCOCO -> NetworkModel.NetworkTypeUI.Rococo
    }

    return NetworkModel(networkType.readableName, type)
}

fun mapCryptoTypeToCryptoTypeModel(
    resourceManager: ResourceManager,
    encryptionType: CryptoType
): CryptoTypeModel {
    val name = when (encryptionType) {
        CryptoType.SR25519 -> "${resourceManager.getString(R.string.sr25519_selection_title)} ${
        resourceManager.getString(
            R.string.sr25519_selection_subtitle
        )
        }"
        CryptoType.ED25519 -> "${resourceManager.getString(R.string.ed25519_selection_title)} ${
        resourceManager.getString(
            R.string.ed25519_selection_subtitle
        )
        }"
        CryptoType.ECDSA -> "${resourceManager.getString(R.string.ecdsa_selection_title)} ${
        resourceManager.getString(
            R.string.ecdsa_selection_subtitle
        )
        }"
    }

    return CryptoTypeModel(name, encryptionType)
}

fun mapNodeToNodeModel(node: Node): NodeModel {
    val networkModelType = mapNetworkTypeToNetworkModel(node.networkType)

    return with(node) {
        NodeModel(
            id = id,
            name = name,
            link = link,
            networkModelType = networkModelType.networkTypeUI,
            isDefault = isDefault,
            isActive = isActive
        )
    }
}

fun mapNodeLocalToNode(nodeLocal: NodeLocal): Node {
    return with(nodeLocal) {
        Node(
            id = id,
            name = name,
            networkType = NetworkType.values()[nodeLocal.networkType],
            link = link,
            isActive = isActive,
            isDefault = isDefault
        )
    }
}

private fun mapMetaAccountTypeFromLocal(local: MetaAccountLocal.Type): LightMetaAccount.Type {
    return when (local) {
        MetaAccountLocal.Type.SECRETS -> LightMetaAccount.Type.SECRETS
        MetaAccountLocal.Type.WATCH_ONLY -> LightMetaAccount.Type.WATCH_ONLY
        MetaAccountLocal.Type.PARITY_SIGNER -> LightMetaAccount.Type.PARITY_SIGNER
        MetaAccountLocal.Type.LEDGER -> LightMetaAccount.Type.LEDGER
    }
}

fun mapMetaAccountWithBalanceFromLocal(local: MetaAccountWithBalanceLocal): MetaAccountAssetBalance {
    return with(local) {
        MetaAccountAssetBalance(
            metaId = id,
            freeInPlanks = freeInPlanks,
            reservedInPlanks = reservedInPlanks,
            offChainBalance = offChainBalance,
            precision = precision,
            rate = rate,
        )
    }
}

fun mapMetaAccountLocalToMetaAccount(
    chainsById: Map<ChainId, Chain>,
    joinedMetaAccountInfo: JoinedMetaAccountInfo
): MetaAccount {
    val chainAccounts = joinedMetaAccountInfo.chainAccounts.associateBy(
        keySelector = ChainAccountLocal::chainId,
        valueTransform = {
            // ignore chainAccounts with unknown chainId
            val chain = chainsById[it.chainId] ?: return@associateBy null

            MetaAccount.ChainAccount(
                metaId = joinedMetaAccountInfo.metaAccount.id,
                chain = chain,
                publicKey = it.publicKey,
                accountId = it.accountId,
                cryptoType = it.cryptoType
            )
        }
    ).filterNotNull()

    return with(joinedMetaAccountInfo.metaAccount) {
        MetaAccount(
            id = id,
            chainAccounts = chainAccounts,
            substratePublicKey = substratePublicKey,
            substrateCryptoType = substrateCryptoType,
            substrateAccountId = substrateAccountId,
            ethereumAddress = ethereumAddress,
            ethereumPublicKey = ethereumPublicKey,
            isSelected = isSelected,
            name = name,
            type = mapMetaAccountTypeFromLocal(type)
        )
    }
}

@Deprecated("Accounts are deprecated")
fun mapMetaAccountToAccount(chain: Chain, metaAccount: MetaAccount): Account? {
    return metaAccount.addressIn(chain)?.let { address ->
        val accountId = chain.hexAccountIdOf(address)

        Account(
            address = address,
            name = metaAccount.name,
            accountIdHex = accountId,
            cryptoType = metaAccount.substrateCryptoType ?: CryptoType.SR25519,
            position = 0,
            network = stubNetwork(chain.id),
        )
    }
}

@Deprecated("Accounts are deprecated")
fun mapChainAccountToAccount(
    parent: MetaAccount,
    chainAccount: MetaAccount.ChainAccount,
): Account {
    val chain = chainAccount.chain

    return Account(
        address = chain.addressOf(chainAccount.accountId),
        name = parent.name,
        accountIdHex = chainAccount.accountId.toHexString(),
        cryptoType = chainAccount.cryptoType ?: CryptoType.SR25519,
        position = 0,
        network = stubNetwork(chain.id),
    )
}


fun mapAddAccountPayloadToAddAccountType(
    payload: AddAccountPayload,
    accountNameState: AccountNameChooserMixin.State,
): AddAccountType {
    return when (payload) {
        AddAccountPayload.MetaAccount -> {
            require(accountNameState is AccountNameChooserMixin.State.Input) { "Name input should be present for meta account" }

            AddAccountType.MetaAccount(accountNameState.value)
        }
        is AddAccountPayload.ChainAccount -> AddAccountType.ChainAccount(payload.chainId, payload.metaId)
    }
}

fun mapOptionalNameToNameChooserState(name: String?) = when (name) {
    null -> AccountNameChooserMixin.State.NoInput
    else -> AccountNameChooserMixin.State.Input(name)
}