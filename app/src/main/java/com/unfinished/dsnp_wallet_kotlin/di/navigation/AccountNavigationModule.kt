package com.unfinished.dsnp_wallet_kotlin.di.navigation

import com.unfinished.dsnp_wallet_kotlin.root.NavigationHolder
import com.unfinished.dsnp_wallet_kotlin.root.Navigator
import com.unfinished.dsnp_wallet_kotlin.root.account.AdvancedEncryptionCommunicatorImpl
import com.unfinished.feature_account.presentation.AccountRouter
import com.unfinished.feature_account.presentation.AdvancedEncryptionCommunicator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.novafoundation.nova.common.resources.ContextManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AccountNavigationModule {

    @Provides
    @Singleton
    fun provideAdvancedEncryptionCommunicator(
        navigationHolder: NavigationHolder
    ): AdvancedEncryptionCommunicator = AdvancedEncryptionCommunicatorImpl(navigationHolder)

    @Provides
    @Singleton
    fun provideAccountRouter(navigator: Navigator): AccountRouter = navigator
}