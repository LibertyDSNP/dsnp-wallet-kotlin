package com.unfinished.runtime.di

import com.google.gson.Gson
import com.unfinished.data.storage.cache.StorageCache
import com.unfinished.data.db.dao.ChainDao
import com.unfinished.data.db.dao.StorageDao
import com.unfinished.data.storage.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.unfinished.runtime.extrinsic.ExtrinsicBuilderFactory
import com.unfinished.runtime.extrinsic.ExtrinsicSerializers
import com.unfinished.runtime.extrinsic.ExtrinsicValidityUseCase
import com.unfinished.runtime.extrinsic.MortalityConstructor
import com.unfinished.runtime.extrinsic.RealExtrinsicValidityUseCase
import com.unfinished.runtime.multiNetwork.ChainRegistry
import com.unfinished.runtime.multiNetwork.qr.MultiChainQrSharingFactory
import com.unfinished.runtime.multiNetwork.runtime.repository.DbRuntimeVersionsRepository
import com.unfinished.runtime.multiNetwork.runtime.repository.EventsRepository
import com.unfinished.runtime.multiNetwork.runtime.repository.RemoteEventsRepository
import com.unfinished.runtime.multiNetwork.runtime.repository.RuntimeVersionsRepository
import com.unfinished.runtime.network.rpc.RpcCalls
import com.unfinished.runtime.network.rpc.bulkRetriever.BulkRetriever
import com.unfinished.runtime.repository.ChainStateRepository
import com.unfinished.runtime.repository.ParachainInfoRepository
import com.unfinished.runtime.repository.RealParachainInfoRepository
import com.unfinished.runtime.repository.RealTotalIssuanceRepository
import com.unfinished.runtime.repository.RemoteTimestampRepository
import com.unfinished.runtime.repository.TimestampRepository
import com.unfinished.runtime.repository.TotalIssuanceRepository
import com.unfinished.data.storage.cache.DbStorageCache
import com.unfinished.runtime.storage.PrefsSampledBlockTimeStorage
import com.unfinished.runtime.storage.SampledBlockTimeStorage
import com.unfinished.runtime.storage.source.LocalStorageSource
import com.unfinished.runtime.storage.source.RemoteStorageSource
import com.unfinished.runtime.storage.source.StorageDataSource
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
