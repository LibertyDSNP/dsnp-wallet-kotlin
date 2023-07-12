package com.unfinished.runtime.di

import com.google.gson.Gson
import com.unfinished.data.db.dao.ChainDao
import com.unfinished.data.file.FileProvider
import com.unfinished.data.network.NetworkApiCreator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.unfinished.runtime.multiNetwork.ChainRegistry
import com.unfinished.runtime.multiNetwork.chain.ChainSyncService
import com.unfinished.runtime.multiNetwork.chain.remote.ChainFetcher
import com.unfinished.runtime.multiNetwork.connection.ChainConnection
import com.unfinished.runtime.multiNetwork.connection.ChainConnectionFactory
import com.unfinished.runtime.multiNetwork.connection.ConnectionPool
import com.unfinished.runtime.multiNetwork.connection.autobalance.NodeAutobalancer
import com.unfinished.runtime.multiNetwork.connection.autobalance.strategy.AutoBalanceStrategyProvider
import com.unfinished.runtime.multiNetwork.runtime.RuntimeFactory
import com.unfinished.runtime.multiNetwork.runtime.RuntimeFilesCache
import com.unfinished.runtime.multiNetwork.runtime.RuntimeProviderPool
import com.unfinished.runtime.multiNetwork.runtime.RuntimeSubscriptionPool
import com.unfinished.runtime.multiNetwork.runtime.RuntimeSyncService
import com.unfinished.runtime.multiNetwork.runtime.types.BaseTypeSynchronizer
import com.unfinished.runtime.multiNetwork.runtime.types.TypesFetcher
import jp.co.soramitsu.fearless_utils.wsrpc.SocketService
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ChainRegistryModule {

    @Provides
    @Singleton
    fun provideChainFetcher(apiCreator: NetworkApiCreator) = apiCreator.create(ChainFetcher::class.java)

    @Provides
    @Singleton
    fun provideChainSyncService(
        dao: ChainDao,
        chainFetcher: ChainFetcher,
        gson: Gson
    ) = ChainSyncService(dao, chainFetcher, gson)

    @Provides
    @Singleton
    fun provideRuntimeFactory(
        runtimeFilesCache: RuntimeFilesCache,
        chainDao: ChainDao,
        gson: Gson,
    ): RuntimeFactory {
        return RuntimeFactory(runtimeFilesCache, chainDao, gson)
    }

    @Provides
    @Singleton
    fun provideRuntimeFilesCache(
        fileProvider: FileProvider,
    ) = RuntimeFilesCache(fileProvider)

    @Provides
    @Singleton
    fun provideTypesFetcher(
        networkApiCreator: NetworkApiCreator,
    ) = networkApiCreator.create(TypesFetcher::class.java)

    @Provides
    @Singleton
    fun provideRuntimeSyncService(
        typesFetcher: TypesFetcher,
        runtimeFilesCache: RuntimeFilesCache,
        chainDao: ChainDao,
    ) = RuntimeSyncService(typesFetcher, runtimeFilesCache, chainDao)

    @Provides
    @Singleton
    fun provideBaseTypeSynchronizer(
        typesFetcher: TypesFetcher,
        runtimeFilesCache: RuntimeFilesCache,
    ) = BaseTypeSynchronizer(runtimeFilesCache, typesFetcher)

    @Provides
    @Singleton
    fun provideRuntimeProviderPool(
        runtimeFactory: RuntimeFactory,
        runtimeSyncService: RuntimeSyncService,
        baseTypeSynchronizer: BaseTypeSynchronizer,
    ) = RuntimeProviderPool(runtimeFactory, runtimeSyncService, baseTypeSynchronizer)

    @Provides
    @Singleton
    fun provideAutoBalanceProvider() = AutoBalanceStrategyProvider()

    @Provides
    @Singleton
    fun provideNodeAutoBalancer(
        autoBalanceStrategyProvider: AutoBalanceStrategyProvider,
    ) = NodeAutobalancer(autoBalanceStrategyProvider)

    @Provides
    @Singleton
    fun provideChainConnectionFactory(
        socketProvider: Provider<SocketService>,
        externalRequirementsFlow: MutableStateFlow<ChainConnection.ExternalRequirement>,
        nodeAutobalancer: NodeAutobalancer,
    ) = ChainConnectionFactory(
        externalRequirementsFlow,
        nodeAutobalancer,
        socketProvider
    )

    @Provides
    @Singleton
    fun provideConnectionPool(chainConnectionFactory: ChainConnectionFactory) = ConnectionPool(chainConnectionFactory)

    @Provides
    @Singleton
    fun provideRuntimeVersionSubscriptionPool(
        chainDao: ChainDao,
        runtimeSyncService: RuntimeSyncService,
    ) = RuntimeSubscriptionPool(chainDao, runtimeSyncService)

    @Provides
    @Singleton
    fun provideExternalRequirementsFlow() = MutableStateFlow(ChainConnection.ExternalRequirement.ALLOWED)

    @Provides
    @Singleton
    fun provideChainRegistry(
        runtimeProviderPool: RuntimeProviderPool,
        chainConnectionPool: ConnectionPool,
        runtimeSubscriptionPool: RuntimeSubscriptionPool,
        chainDao: ChainDao,
        chainSyncService: ChainSyncService,
        baseTypeSynchronizer: BaseTypeSynchronizer,
        runtimeSyncService: RuntimeSyncService,
        gson: Gson
    ) = ChainRegistry(
        runtimeProviderPool,
        chainConnectionPool,
        runtimeSubscriptionPool,
        chainDao,
        chainSyncService,
        baseTypeSynchronizer,
        runtimeSyncService,
        gson
    )
}
