package com.unfinished.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.unfinished.data.secrets.v2.SecretStoreV2
import com.unfinished.data.signer.RealSignerProvider
import com.unfinished.data.signer.SignerProvider
import com.unfinished.data.signer.secrets.SecretsSignerFactory
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
