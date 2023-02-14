package com.unfinished.dsnp_wallet_kotlin.di.navigation

import com.unfinished.dsnp_wallet_kotlin.root.NavigationHolder
import com.unfinished.dsnp_wallet_kotlin.root.Navigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.novafoundation.nova.common.resources.ContextManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NavigationModule {

    @Provides
    @Singleton
    fun provideNavigatorHolder(
        contextManager: ContextManager
    ): NavigationHolder = NavigationHolder(contextManager)

    @Provides
    @Singleton
    fun provideNavigator(
        navigatorHolder: NavigationHolder
    ): Navigator = Navigator(navigatorHolder)

    /*
    @Provides
    @Singleton
    fun provideRootRouter(navigator: Navigator): RootRouter = navigator

    @Provides
    @Singleton
    fun provideSplashRouter(navigator: Navigator): SplashRouter = navigator

    @Provides
    @Singleton
    fun provideOnboardingRouter(navigator: Navigator): OnboardingRouter = navigator

    */
}
