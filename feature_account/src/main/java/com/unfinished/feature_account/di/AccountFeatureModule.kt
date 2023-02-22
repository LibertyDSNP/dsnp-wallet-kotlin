package com.unfinished.feature_account.di

import androidx.navigation.Navigator
import com.google.gson.Gson
import com.unfinished.feature_account.BuildConfig
import com.unfinished.feature_account.data.blockchain.AccountSubstrateSource
import com.unfinished.feature_account.data.blockchain.AccountSubstrateSourceImpl
import com.unfinished.feature_account.data.extrinsic.ExtrinsicService
import com.unfinished.feature_account.data.extrinsic.RealExtrinsicService
import com.unfinished.feature_account.data.repository.AccountRepositoryImpl
import com.unfinished.feature_account.data.repository.AddAccountRepository
import com.unfinished.feature_account.data.repository.datasource.AccountDataSourceImpl
import com.unfinished.feature_account.data.repository.datasource.migration.AccountDataMigration
import com.unfinished.feature_account.data.secrets.AccountSecretsFactory
import com.unfinished.feature_account.data.signer.SignerProvider
import com.unfinished.feature_account.domain.account.add.AddAccountInteractor
import com.unfinished.feature_account.domain.account.details.AccountDetailsInteractor
import com.unfinished.feature_account.domain.interactor.AccountInteractorImpl
import com.unfinished.feature_account.domain.interactor.AdvancedEncryptionInteractor
import com.unfinished.feature_account.domain.interactor.MetaAccountGroupingInteractorImpl
import com.unfinished.feature_account.domain.interfaces.*
import com.unfinished.feature_account.domain.updaters.AccountUpdateScope
import com.unfinished.feature_account.presentation.AdvancedEncryptionCommunicator
import com.unfinished.feature_account.presentation.action.ExternalActions
import com.unfinished.feature_account.presentation.action.ExternalActionsProvider
import com.unfinished.feature_account.presentation.icon.AddressDisplayUseCase
import com.unfinished.feature_account.presentation.mixin.addressInput.AddressInputMixinFactory
import com.unfinished.feature_account.presentation.mixin.importType.ImportTypeChooserMixin
import com.unfinished.feature_account.presentation.mixin.importType.ImportTypeChooserProvider
import com.unfinished.feature_account.presentation.mnemonic.confirm.ConfirmMnemonicConfig
import com.unfinished.feature_account.presentation.wallet.WalletUiUseCase
import com.unfinished.feature_account.presentation.wallet.WalletUiUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.novafoundation.nova.common.address.AddressIconGenerator
import io.novafoundation.nova.common.data.network.rpc.SocketSingleRequestExecutor
import io.novafoundation.nova.common.data.secrets.v1.SecretStoreV1
import io.novafoundation.nova.common.data.secrets.v2.SecretStoreV2
import io.novafoundation.nova.common.data.storage.Preferences
import io.novafoundation.nova.common.data.storage.encrypt.EncryptedPreferences
import io.novafoundation.nova.common.resources.ClipboardManager
import io.novafoundation.nova.common.resources.LanguagesHolder
import io.novafoundation.nova.common.resources.ResourceManager
import io.novafoundation.nova.common.utils.systemCall.SystemCallExecutor
import io.novafoundation.nova.core_db.dao.AccountDao
import io.novafoundation.nova.core_db.dao.MetaAccountDao
import io.novafoundation.nova.core_db.dao.NodeDao
import io.novafoundation.nova.runtime.extrinsic.ExtrinsicBuilderFactory
import io.novafoundation.nova.runtime.multiNetwork.ChainRegistry
import io.novafoundation.nova.runtime.multiNetwork.qr.MultiChainQrSharingFactory
import io.novafoundation.nova.runtime.network.rpc.RpcCalls
import jp.co.soramitsu.fearless_utils.encrypt.json.JsonSeedDecoder
import jp.co.soramitsu.fearless_utils.encrypt.json.JsonSeedEncoder
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AccountFeatureModule {

    @Provides
    fun provideExtrinsicService(
        accountRepository: AccountRepository,
        rpcCalls: RpcCalls,
        extrinsicBuilderFactory: ExtrinsicBuilderFactory,
        signerProvider: SignerProvider
    ): ExtrinsicService = RealExtrinsicService(
        rpcCalls,
        accountRepository,
        extrinsicBuilderFactory,
        signerProvider
    )

    @Provides
    fun provideJsonDecoder(jsonMapper: Gson) = JsonSeedDecoder(jsonMapper)

    @Provides
    fun provideJsonEncoder(
        jsonMapper: Gson,
    ) = JsonSeedEncoder(jsonMapper)

    @Provides
    fun provideAccountRepository(
        accountDataSource: AccountDataSource,
        accountDao: AccountDao,
        nodeDao: NodeDao,
        jsonSeedEncoder: JsonSeedEncoder,
        accountSubstrateSource: AccountSubstrateSource,
        languagesHolder: LanguagesHolder,
        secretStoreV2: SecretStoreV2,
        multiChainQrSharingFactory: MultiChainQrSharingFactory,
    ): AccountRepository {
        return AccountRepositoryImpl(
            accountDataSource,
            accountDao,
            nodeDao,
            jsonSeedEncoder,
            languagesHolder,
            accountSubstrateSource,
            secretStoreV2,
            multiChainQrSharingFactory
        )
    }

    @Provides
    fun provideAccountInteractor(
        chainRegistry: ChainRegistry,
        accountRepository: AccountRepository,
    ): AccountInteractor {
        return AccountInteractorImpl(chainRegistry, accountRepository)
    }

    @Provides
    fun provideAccountDataSource(
        preferences: Preferences,
        encryptedPreferences: EncryptedPreferences,
        nodeDao: NodeDao,
        secretStoreV1: SecretStoreV1,
        accountDataMigration: AccountDataMigration,
        metaAccountDao: MetaAccountDao,
        chainRegistry: ChainRegistry,
        secretStoreV2: SecretStoreV2,
    ): AccountDataSource {
        return AccountDataSourceImpl(
            preferences,
            encryptedPreferences,
            nodeDao,
            metaAccountDao,
            chainRegistry,
            secretStoreV2,
            secretStoreV1,
            accountDataMigration
        )
    }

    @Provides
    fun provideAccountSubstrateSource(socketRequestExecutor: SocketSingleRequestExecutor): AccountSubstrateSource {
        return AccountSubstrateSourceImpl(socketRequestExecutor)
    }

    @Provides
    fun provideAccountDataMigration(
        preferences: Preferences,
        encryptedPreferences: EncryptedPreferences,
        accountDao: AccountDao,
    ): AccountDataMigration {
        return AccountDataMigration(preferences, encryptedPreferences, accountDao)
    }

    @Provides
    fun provideExternalAccountActions(
        clipboardManager: ClipboardManager,
        resourceManager: ResourceManager,
        addressIconGenerator: AddressIconGenerator
    ): ExternalActions.Presentation {
        return ExternalActionsProvider(clipboardManager, resourceManager, addressIconGenerator)
    }

    @Provides
    fun provideAccountUpdateScope(
        accountRepository: AccountRepository,
    ) = AccountUpdateScope(accountRepository)

    @Provides
    fun provideAddressDisplayUseCase(
        accountRepository: AccountRepository,
    ) = AddressDisplayUseCase(accountRepository)

    @Provides
    fun provideAccountUseCase(
        accountRepository: AccountRepository,
        addressIconGenerator: AddressIconGenerator,
        walletUiUseCase: WalletUiUseCase,
    ) = SelectedAccountUseCase(accountRepository, walletUiUseCase, addressIconGenerator)

    @Provides
    fun provideAccountDetailsInteractor(
        accountRepository: AccountRepository,
        secretStoreV2: SecretStoreV2,
        chainRegistry: ChainRegistry,
    ) = AccountDetailsInteractor(
        accountRepository,
        secretStoreV2,
        chainRegistry
    )

    @Provides
    fun provideAccountSecretsFactory(
        jsonSeedDecoder: JsonSeedDecoder
    ) = AccountSecretsFactory(jsonSeedDecoder)

    @Provides
    fun provideAddAccountRepository(
        accountDataSource: AccountDataSource,
        accountSecretsFactory: AccountSecretsFactory,
        jsonSeedDecoder: JsonSeedDecoder,
        chainRegistry: ChainRegistry,
    ) = AddAccountRepository(
        accountDataSource,
        accountSecretsFactory,
        jsonSeedDecoder,
        chainRegistry
    )

    @Provides
    fun provideAddAccountInteractor(
        addAccountRepository: AddAccountRepository,
        accountRepository: AccountRepository,
    ) = AddAccountInteractor(addAccountRepository, accountRepository)

    @Provides
    fun provideInteractor(
        accountRepository: AccountRepository,
        secretStoreV2: SecretStoreV2,
        chainRegistry: ChainRegistry,
    ) = AdvancedEncryptionInteractor(accountRepository, secretStoreV2, chainRegistry)

    @Provides
    fun provideImportTypeChooserMixin(): ImportTypeChooserMixin.Presentation = ImportTypeChooserProvider()

    @Provides
    fun provideAddressInputMixinFactory(
        addressIconGenerator: AddressIconGenerator,
        systemCallExecutor: SystemCallExecutor,
        clipboardManager: ClipboardManager,
        multiChainQrSharingFactory: MultiChainQrSharingFactory,
        resourceManager: ResourceManager,
        accountUseCase: SelectedAccountUseCase
    ) = AddressInputMixinFactory(
        addressIconGenerator = addressIconGenerator,
        systemCallExecutor = systemCallExecutor,
        clipboardManager = clipboardManager,
        qrSharingFactory = multiChainQrSharingFactory,
        resourceManager = resourceManager,
        accountUseCase = accountUseCase
    )

    @Provides
    fun provideWalletUiUseCase(
        accountRepository: AccountRepository,
        addressIconGenerator: AddressIconGenerator
    ): WalletUiUseCase {
        return WalletUiUseCaseImpl(accountRepository, addressIconGenerator)
    }

    @Provides
    fun provideMetaAccountGroupingInteractor(
        chainRegistry: ChainRegistry,
        accountRepository: AccountRepository
    ): MetaAccountGroupingInteractor {
        return MetaAccountGroupingInteractorImpl(chainRegistry, accountRepository)
    }

    @Provides
    fun provideConfig() = ConfirmMnemonicConfig(
        allowShowingSkip = BuildConfig.DEBUG
    )


}