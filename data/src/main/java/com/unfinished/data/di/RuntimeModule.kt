package com.unfinished.data.di

import com.google.gson.Gson
import com.unfinished.data.storage.cache.StorageCache
import com.unfinished.data.db.dao.ChainDao
import com.unfinished.data.db.dao.StorageDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.unfinished.data.multiNetwork.extrinsic.ExtrinsicBuilderFactory
import com.unfinished.data.multiNetwork.extrinsic.ExtrinsicSerializers
import com.unfinished.data.multiNetwork.extrinsic.ExtrinsicValidityUseCase
import com.unfinished.data.multiNetwork.extrinsic.MortalityConstructor
import com.unfinished.data.multiNetwork.extrinsic.RealExtrinsicValidityUseCase
import com.unfinished.data.multiNetwork.ChainRegistry
import com.unfinished.data.multiNetwork.qr.MultiChainQrSharingFactory
import com.unfinished.data.repository.runtime.DbRuntimeVersionsRepository
import com.unfinished.data.repository.event.EventsRepository
import com.unfinished.data.repository.event.RemoteEventsRepository
import com.unfinished.data.repository.runtime.RuntimeVersionsRepository
import com.unfinished.data.multiNetwork.rpc.RpcCalls
import com.unfinished.data.multiNetwork.rpc.bulkRetriever.BulkRetriever
import com.unfinished.data.repository.chain.ChainStateRepository
import com.unfinished.data.repository.chain.ParachainInfoRepository
import com.unfinished.data.repository.chain.RealParachainInfoRepository
import com.unfinished.data.repository.RealTotalIssuanceRepository
import com.unfinished.data.repository.RemoteTimestampRepository
import com.unfinished.data.repository.TimestampRepository
import com.unfinished.data.repository.TotalIssuanceRepository
import com.unfinished.data.storage.cache.DbStorageCache
import com.unfinished.data.multiNetwork.storage.PrefsSampledBlockTimeStorage
import com.unfinished.data.multiNetwork.storage.SampledBlockTimeStorage
import com.unfinished.data.multiNetwork.storage.source.LocalStorageSource
import com.unfinished.data.multiNetwork.storage.source.RemoteStorageSource
import com.unfinished.data.multiNetwork.storage.source.StorageDataSource
import com.unfinished.data.storage.Preferences
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

const val LOCAL_STORAGE_SOURCE = "LOCAL_STORAGE_SOURCE"
const val REMOTE_STORAGE_SOURCE = "REMOTE_STORAGE_SOURCE"
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ExtrinsicSerialization
@Module
@InstallIn(SingletonComponent::class)
class RuntimeModule {

    @Provides
    @Singleton
    fun provideExtrinsicBuilderFactory(
        rpcCalls: RpcCalls,
        chainRegistry: ChainRegistry,
        mortalityConstructor: MortalityConstructor,
    ) = ExtrinsicBuilderFactory(
        rpcCalls,
        chainRegistry,
        mortalityConstructor
    )

    @Provides
    @Singleton
    fun provideStorageCache(
        storageDao: StorageDao,
    ): StorageCache = DbStorageCache(storageDao)

    @Provides
    @Named(LOCAL_STORAGE_SOURCE)
    @Singleton
    fun provideLocalStorageSource(
        chainRegistry: ChainRegistry,
        storageCache: StorageCache,
    ): StorageDataSource = LocalStorageSource(chainRegistry, storageCache)

    @Provides
    @Named(REMOTE_STORAGE_SOURCE)
    @Singleton
    fun provideRemoteStorageSource(
        chainRegistry: ChainRegistry,
        bulkRetriever: BulkRetriever,
    ): StorageDataSource = RemoteStorageSource(chainRegistry, bulkRetriever)

    @Provides
    @Singleton
    fun provideSampledBlockTimeStorage(
        gson: Gson,
        preferences: Preferences,
    ): SampledBlockTimeStorage = PrefsSampledBlockTimeStorage(gson, preferences)

    @Provides
    @Singleton
    fun provideChainStateRepository(
        @Named(LOCAL_STORAGE_SOURCE) localStorageSource: StorageDataSource,
        sampledBlockTimeStorage: SampledBlockTimeStorage,
        chainRegistry: ChainRegistry
    ) = ChainStateRepository(localStorageSource, sampledBlockTimeStorage, chainRegistry)

    @Provides
    @Singleton
    fun provideMortalityProvider(
        chainStateRepository: ChainStateRepository,
        rpcCalls: RpcCalls,
    ) = MortalityConstructor(rpcCalls, chainStateRepository)

    @Provides
    @Singleton
    fun provideSubstrateCalls(
        chainRegistry: ChainRegistry
    ) = RpcCalls(chainRegistry)

    @Provides
    @Singleton
    @ExtrinsicSerialization
    fun provideExtrinsicGson() = ExtrinsicSerializers.gson()

    @Provides
    @Singleton
    fun provideRuntimeVersionsRepository(
        chainDao: ChainDao
    ): RuntimeVersionsRepository = DbRuntimeVersionsRepository(chainDao)

    @Provides
    @Singleton
    fun provideEventsRepository(
        rpcCalls: RpcCalls,
        chainRegistry: ChainRegistry,
        @Named(REMOTE_STORAGE_SOURCE) remoteStorageSource: StorageDataSource
    ): EventsRepository = RemoteEventsRepository(rpcCalls, chainRegistry, remoteStorageSource)

    @Provides
    @Singleton
    fun provideMultiChainQrSharingFactory() = MultiChainQrSharingFactory()

    @Provides
    @Singleton
    fun provideParachainInfoRepository(
        @Named(REMOTE_STORAGE_SOURCE) remoteStorageSource: StorageDataSource
    ): ParachainInfoRepository = RealParachainInfoRepository(remoteStorageSource)

    @Provides
    @Singleton
    fun provideExtrinsicValidityUseCase(
        mortalityConstructor: MortalityConstructor
    ): ExtrinsicValidityUseCase = RealExtrinsicValidityUseCase(mortalityConstructor)

    @Provides
    @Singleton
    fun provideTimestampRepository(
        @Named(REMOTE_STORAGE_SOURCE) remoteStorageSource: StorageDataSource,
    ): TimestampRepository = RemoteTimestampRepository(remoteStorageSource)

    @Provides
    @Singleton
    fun provideTotalIssuanceRepository(
        @Named(LOCAL_STORAGE_SOURCE) localStorageSource: StorageDataSource,
    ): TotalIssuanceRepository = RealTotalIssuanceRepository(localStorageSource)

    @Provides
    @Singleton
    fun provideDefaultPagedKeysRetriever(): BulkRetriever {
        return BulkRetriever()
    }
}
