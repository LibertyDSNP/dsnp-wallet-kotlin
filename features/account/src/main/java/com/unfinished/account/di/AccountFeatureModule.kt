package com.unfinished.account.di

import com.google.gson.Gson
import com.unfinished.account.BuildConfig
import com.unfinished.account.data.repository.AccountRepositoryImpl
import com.unfinished.account.data.repository.AddAccountRepository
import com.unfinished.account.data.repository.datasource.AccountDataSourceImpl
import com.unfinished.account.data.repository.datasource.migration.AccountDataMigration
import com.unfinished.account.data.secrets.AccountSecretsFactory
import com.unfinished.account.domain.account.add.AddAccountInteractor
import com.unfinished.account.domain.account.details.AccountDetailsInteractor
import com.unfinished.account.domain.interactor.AccountInteractorImpl
import com.unfinished.account.domain.interactor.AdvancedEncryptionInteractor
import com.unfinished.account.domain.interactor.MetaAccountGroupingInteractorImpl
import com.unfinished.account.domain.interfaces.*
import com.unfinished.account.domain.updaters.AccountUpdateScope
import com.unfinished.account.presentation.action.ExternalActions
import com.unfinished.account.presentation.action.ExternalActionsProvider
import com.unfinished.account.presentation.icon.AddressDisplayUseCase
import com.unfinished.account.presentation.mixin.addressInput.AddressInputMixinFactory
import com.unfinished.account.presentation.mixin.importType.ImportTypeChooserMixin
import com.unfinished.account.presentation.mixin.importType.ImportTypeChooserProvider
import com.unfinished.account.presentation.mnemonic.confirm.ConfirmMnemonicConfig
import com.unfinished.account.presentation.wallet.WalletUiUseCase
import com.unfinished.account.presentation.wallet.WalletUiUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.unfinished.common.address.AddressIconGenerator
import com.unfinished.common.pref.CommonPreferences
import com.unfinished.data.secrets.v1.SecretStoreV1
import com.unfinished.data.secrets.v2.SecretStoreV2
import com.unfinished.data.storage.encrypt.EncryptedPreferences
import com.unfinished.common.resources.ClipboardManager
import com.unfinished.common.resources.LanguagesHolder
import com.unfinished.common.resources.ResourceManager
import com.unfinished.common.utils.systemCall.SystemCallExecutor
import com.unfinished.data.db.dao.AccountDao
import com.unfinished.data.db.dao.MetaAccountDao
import com.unfinished.data.db.dao.NodeDao
import com.unfinished.data.multiNetwork.ChainRegistry
import com.unfinished.data.multiNetwork.extrinsic.ExtrinsicBuilderFactory
import com.unfinished.data.multiNetwork.extrinsic.blockchain.AccountSubstrateSource
import com.unfinished.data.multiNetwork.extrinsic.blockchain.AccountSubstrateSourceImpl
import com.unfinished.data.multiNetwork.extrinsic.service.ExtrinsicService
import com.unfinished.data.multiNetwork.extrinsic.service.RealExtrinsicService
import com.unfinished.data.multiNetwork.qr.MultiChainQrSharingFactory
import com.unfinished.data.multiNetwork.rpc.RpcCalls
import com.unfinished.data.multiNetwork.rpc.SocketSingleRequestExecutor
import com.unfinished.data.signer.SignerProvider
import com.unfinished.data.storage.Preferences
import jp.co.soramitsu.fearless_utils.encrypt.json.JsonSeedDecoder
import jp.co.soramitsu.fearless_utils.encrypt.json.JsonSeedEncoder

@Module
@InstallIn(SingletonComponent::class)
object AccountFeatureModule {

    @Provides
    fun provideExtrinsicService(
        rpcCalls: RpcCalls,
        extrinsicBuilderFactory: ExtrinsicBuilderFactory,
        signerProvider: SignerProvider
    ): ExtrinsicService = RealExtrinsicService(
        rpcCalls,
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
        preferences: CommonPreferences,
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