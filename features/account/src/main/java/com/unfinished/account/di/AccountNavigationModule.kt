package com.unfinished.account.di

import com.unfinished.account.presentation.AccountRouter
import com.unfinished.account.presentation.AdvancedEncryptionCommunicator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AccountNavigationModule {

    @Provides
    @Singleton
    fun provideAdvancedEncryptionCommunicator(): AdvancedEncryptionCommunicator =
        AdvancedEncryptionCommunicatorImpl()

    @Provides
    @Singleton
    fun provideAccountRouter(navigator: Navigator): AccountRouter = navigator
}
