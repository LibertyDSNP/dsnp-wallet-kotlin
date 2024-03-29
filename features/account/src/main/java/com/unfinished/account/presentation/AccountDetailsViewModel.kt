package com.unfinished.account.presentation

import androidx.lifecycle.viewModelScope
import com.unfinished.account.data.mappers.mapChainToUi
import com.unfinished.common.R
import com.unfinished.account.domain.account.details.AccountDetailsInteractor
import com.unfinished.account.domain.account.details.AccountInChain
import com.unfinished.account.domain.model.LightMetaAccount
import com.unfinished.account.presentation.action.ExternalActions
import com.unfinished.account.presentation.export.ExportPayload
import com.unfinished.account.presentation.mixin.importType.ImportTypeChooserMixin
import com.unfinished.account.presentation.model.account.add.SecretType
import com.unfinished.account.presentation.model.account.details.AccountTypeAlert
import com.unfinished.account.presentation.model.account.details.ChainAccountActionsSheet
import com.unfinished.account.presentation.model.common.chainAccount.AccountInChainUi
import com.unfinished.common.address.AddressIconGenerator
import com.unfinished.common.base.BaseViewModel
import com.unfinished.common.list.TextHeader
import com.unfinished.common.resources.ResourceManager
import com.unfinished.data.util.filterToSet
import com.unfinished.common.utils.flowOf
import com.unfinished.common.view.AlertView
import com.unfinished.data.util.invoke
import com.unfinished.runtime.multiNetwork.ChainRegistry
import com.unfinished.runtime.multiNetwork.chain.model.Chain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

private const val UPDATE_NAME_INTERVAL_SECONDS = 1L

class AccountDetailsViewModel(
    private val interactor: AccountDetailsInteractor,
    private val accountRouter: AccountRouter,
    private val iconGenerator: AddressIconGenerator,
    private val resourceManager: ResourceManager,
    private val metaId: Long,
    private val externalActions: ExternalActions.Presentation,
    private val chainRegistry: ChainRegistry,
    private val importTypeChooserMixin: ImportTypeChooserMixin.Presentation,
) : BaseViewModel(),
    ExternalActions by externalActions,
    ImportTypeChooserMixin by importTypeChooserMixin {

    val accountNameFlow: MutableStateFlow<String> = MutableStateFlow("")

    private val metaAccount = async(Dispatchers.Default) { interactor.getMetaAccount(metaId) }

    val availableAccountActions = flowOf {
        availableAccountActions(metaAccount().type)
    }.shareInBackground()

    val typeAlert = flowOf {
        accountTypeAlertFor(metaAccount().type)
    }.shareInBackground()

    init {
        launch {
            accountNameFlow.emit(metaAccount().name)
        }

        syncNameChangesWithDb()
    }

    fun backClicked() {
        accountRouter.back()
    }

    private fun syncNameChangesWithDb() {
        accountNameFlow
            .filter { it.isNotEmpty() }
            .debounce(UPDATE_NAME_INTERVAL_SECONDS.seconds)
            .onEach { interactor.updateName(metaId, it) }
            .launchIn(viewModelScope)
    }

    private suspend fun mapFromToTextHeader(from: AccountInChain.From): TextHeader? {
        return when (metaAccount().type) {
            LightMetaAccount.Type.LEDGER, LightMetaAccount.Type.PARITY_SIGNER -> null
            LightMetaAccount.Type.SECRETS, LightMetaAccount.Type.WATCH_ONLY -> {
                val resId = when (from) {
                    AccountInChain.From.META_ACCOUNT -> R.string.account_shared_secret
                    AccountInChain.From.CHAIN_ACCOUNT -> R.string.account_custom_secret
                }

                return TextHeader(resourceManager.getString(resId))
            }
        }
    }

    private suspend fun mapChainAccountProjectionToUi(metaAccount: LightMetaAccount, accountInChain: AccountInChain) = with(accountInChain) {
        val addressOrHint = when {
            projection != null -> projection.address
            metaAccount.type == LightMetaAccount.Type.PARITY_SIGNER -> resourceManager.getString(R.string.account_details_parity_signer_not_supported)
            else -> resourceManager.getString(R.string.account_no_chain_projection)
        }

        val accountIcon = projection?.let {
            iconGenerator.createAddressIcon(it.accountId, AddressIconGenerator.SIZE_SMALL, backgroundColorRes = AddressIconGenerator.BACKGROUND_TRANSPARENT)
        } ?: resourceManager.getDrawable(R.drawable.ic_warning_filled)

        val availableActionsForChain = availableActionsFor(accountInChain)
        val canViewAddresses = accountInChain.projection != null
        val canDoAnyActions = availableActionsForChain.isNotEmpty() || canViewAddresses

        AccountInChainUi(
            chainUi = mapChainToUi(chain),
            addressOrHint = addressOrHint,
            address = projection?.address,
            accountIcon = accountIcon,
            actionsAvailable = canDoAnyActions
        )
    }

    fun chainAccountClicked(item: AccountInChainUi) = launch {
        if (!item.actionsAvailable) return@launch

        val chain = chainRegistry.getChain(item.chainUi.id)

        val type = ExternalActions.Type.Address(item.address)

        externalActions.showExternalActions(type, chain)
    }

    fun exportClicked(inChain: Chain) = launch {
        viewModelScope.launch {
            val sources = interactor.availableExportTypes(metaAccount(), inChain)

            val payload = ImportTypeChooserMixin.Payload(
                allowedTypes = sources,
                onChosen = { exportTypeChosen(it, inChain) }
            )
            importTypeChooserMixin.showChooser(payload)
        }
    }

    fun changeChainAccountClicked(inChain: Chain) {

    }

    private fun availableAccountActions(accountType: LightMetaAccount.Type): Set<ChainAccountActionsSheet.AccountAction> {
        return when (accountType) {
            LightMetaAccount.Type.SECRETS -> setOf(ChainAccountActionsSheet.AccountAction.EXPORT, ChainAccountActionsSheet.AccountAction.CHANGE)
            LightMetaAccount.Type.WATCH_ONLY -> setOf(ChainAccountActionsSheet.AccountAction.CHANGE)
            LightMetaAccount.Type.PARITY_SIGNER -> emptySet()
            LightMetaAccount.Type.LEDGER -> setOf(ChainAccountActionsSheet.AccountAction.CHANGE)
        }
    }

    private fun accountTypeAlertFor(accountType:  LightMetaAccount.Type): AccountTypeAlert? {
        return when (accountType) {
            LightMetaAccount.Type.WATCH_ONLY -> AccountTypeAlert(
                style = AlertView.Style(
                    backgroundColorRes = R.color.block_background,
                    iconRes = R.drawable.ic_watch_only_filled
                ),
                text = resourceManager.getString(R.string.account_details_watch_only_alert)
            )
            LightMetaAccount.Type.PARITY_SIGNER -> AccountTypeAlert(
                style = AlertView.Style(
                    backgroundColorRes = R.color.block_background,
                    iconRes = R.drawable.ic_parity_signer
                ),
                text = resourceManager.getString(R.string.account_details_parity_signer_alert)
            )
            LightMetaAccount.Type.SECRETS -> null
            LightMetaAccount.Type.LEDGER -> AccountTypeAlert(
                style = AlertView.Style(
                    backgroundColorRes = R.color.block_background,
                    iconRes = R.drawable.ic_ledger
                ),
                text = resourceManager.getString(R.string.ledger_wallet_details_description)
            )
        }
    }

    private fun exportTypeChosen(type: SecretType, chain: Chain) {
        val exportPayload = ExportPayload(metaId, chain.id)

        val navigationAction = when (type) {
            SecretType.MNEMONIC -> accountRouter.exportMnemonicAction(exportPayload)
            SecretType.SEED -> accountRouter.exportSeedAction(exportPayload)
            SecretType.JSON -> accountRouter.exportJsonPasswordAction(exportPayload)
        }

        accountRouter.withPinCodeCheckRequired(navigationAction)
    }

    private suspend fun availableActionsFor(accountInChain: AccountInChain): Set<ChainAccountActionsSheet.AccountAction> {
        return availableAccountActions.first().filterToSet { action ->
            when (action) {
                ChainAccountActionsSheet.AccountAction.CHANGE -> true
                ChainAccountActionsSheet.AccountAction.EXPORT -> accountInChain.projection != null
            }
        }
    }
}
