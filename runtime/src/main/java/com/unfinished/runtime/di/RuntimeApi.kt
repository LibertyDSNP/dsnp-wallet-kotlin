package com.unfinished.runtime.di

import com.google.gson.Gson
import com.unfinished.common.core.api.storage.StorageCache
import com.unfinished.runtime.extrinsic.ExtrinsicBuilderFactory
import com.unfinished.runtime.extrinsic.ExtrinsicValidityUseCase
import com.unfinished.runtime.extrinsic.MortalityConstructor
import com.unfinished.runtime.multiNetwork.ChainRegistry
import com.unfinished.runtime.multiNetwork.chain.ChainSyncService
import com.unfinished.runtime.multiNetwork.connection.ChainConnection
import com.unfinished.runtime.multiNetwork.qr.MultiChainQrSharingFactory
import com.unfinished.runtime.multiNetwork.runtime.repository.EventsRepository
import com.unfinished.runtime.multiNetwork.runtime.repository.RuntimeVersionsRepository
import com.unfinished.runtime.network.rpc.RpcCalls
import com.unfinished.runtime.repository.ChainStateRepository
import com.unfinished.runtime.repository.ParachainInfoRepository
import com.unfinished.runtime.repository.TimestampRepository
import com.unfinished.runtime.repository.TotalIssuanceRepository
import com.unfinished.runtime.storage.SampledBlockTimeStorage
import com.unfinished.runtime.storage.source.StorageDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Named
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ExtrinsicSerialization

interface RuntimeApi {

    fun provideExtrinsicBuilderFactory(): ExtrinsicBuilderFactory

    fun externalRequirementFlow(): MutableStateFlow<ChainConnection.ExternalRequirement>

    fun storageCache(): StorageCache

    @Named(REMOTE_STORAGE_SOURCE)
    fun remoteStorageSource(): StorageDataSource

    @Named(LOCAL_STORAGE_SOURCE)
    fun localStorageSource(): StorageDataSource

    fun chainSyncService(): ChainSyncService

    fun chainStateRepository(): ChainStateRepository

    fun chainRegistry(): ChainRegistry

    fun rpcCalls(): RpcCalls

    @ExtrinsicSerialization
    fun extrinsicGson(): Gson

    fun runtimeVersionsRepository(): RuntimeVersionsRepository

    fun eventsRepository(): EventsRepository

    val multiChainQrSharingFactory: MultiChainQrSharingFactory

    val sampledBlockTime: SampledBlockTimeStorage

    val parachainInfoRepository: ParachainInfoRepository

    val mortalityConstructor: MortalityConstructor

    val extrinsicValidityUseCase: ExtrinsicValidityUseCase

    val timestampRepository: TimestampRepository

    val totalIssuanceRepository: TotalIssuanceRepository
}
