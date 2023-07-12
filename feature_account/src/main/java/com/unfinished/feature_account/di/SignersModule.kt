package com.unfinished.feature_account.di

import com.unfinished.feature_account.data.signer.RealSignerProvider
import com.unfinished.feature_account.data.signer.SignerProvider
import com.unfinished.feature_account.data.signer.secrets.SecretsSignerFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.components.SingletonComponent
import com.unfinished.data.secrets.v2.SecretStoreV2
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SignersModule {

    @Provides
    @Singleton
    fun provideSecretsSignerFactory(secretStoreV2: SecretStoreV2) = SecretsSignerFactory(secretStoreV2)

    @Provides
    @Singleton
    fun provideSignerProvider(
        secretsSignerFactory: SecretsSignerFactory,
    ): SignerProvider = RealSignerProvider(
        secretsSignerFactory = secretsSignerFactory
    )
}
