package com.unfinished.feature_account.di

import com.unfinished.feature_account.domain.account.export.json.ExportJsonInteractor
import com.unfinished.feature_account.domain.account.export.mnemonic.ExportMnemonicInteractor
import com.unfinished.feature_account.domain.account.export.seed.ExportSeedInteractor
import com.unfinished.feature_account.domain.interfaces.AccountRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.unfinished.common.data.secrets.v2.SecretStoreV2
import com.unfinished.runtime.multiNetwork.ChainRegistry
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExportModule {

    @Provides
    @Singleton
    fun provideExportJsonInteractor(
        accountRepository: AccountRepository,
        chainRegistry: ChainRegistry,
    ) = ExportJsonInteractor(
        accountRepository,
        chainRegistry
    )

    @Provides
    @Singleton
    fun provideExportMnemonicInteractor(
        accountRepository: AccountRepository,
        chainRegistry: ChainRegistry,
        secretStoreV2: SecretStoreV2,
    ) = ExportMnemonicInteractor(
        accountRepository,
        secretStoreV2,
        chainRegistry
    )

    @Provides
    @Singleton
    fun provideExportSeedInteractor(
        accountRepository: AccountRepository,
        chainRegistry: ChainRegistry,
        secretStoreV2: SecretStoreV2,
    ) = ExportSeedInteractor(
        accountRepository,
        secretStoreV2,
        chainRegistry
    )
}
