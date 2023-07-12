package com.unfinished.data.di

import android.content.Context
import com.google.gson.Gson
import com.neovisionaries.ws.client.WebSocketFactory
import com.unfinished.data.network.AndroidLogger
import com.unfinished.data.network.HttpExceptionHandler
import com.unfinished.data.network.NetworkApiCreator
import com.unfinished.data.network.TimeHeaderInterceptor
import com.unfinished.data.network.rpc.SocketSingleRequestExecutor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.unfinished.data.BuildConfig
import com.unfinished.data.util.resource.ResourceManager
import com.unfinished.data.util.resource.ResourceManagerImpl
import jp.co.soramitsu.fearless_utils.wsrpc.SocketService
import jp.co.soramitsu.fearless_utils.wsrpc.logging.Logger
import jp.co.soramitsu.fearless_utils.wsrpc.recovery.Reconnector
import jp.co.soramitsu.fearless_utils.wsrpc.request.RequestExecutor
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val HTTP_CACHE = "http_cache"
private const val CACHE_SIZE = 50L * 1024L * 1024L // 50 MiB
private const val TIMEOUT_SECONDS = 20L

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .cache(Cache(File(context.cacheDir, HTTP_CACHE), CACHE_SIZE))
            .retryOnConnectionFailure(true)
            .addInterceptor(TimeHeaderInterceptor())

        /**
         * If you need bodies of payloads from restful calls to be logged, feel free to uncomment
         * these lines for your local build only.
         */
//        if (BuildConfig.DEBUG) {
//            builder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
//        }

        return builder.build()
    }

    @Provides
    @Singleton
    fun provideLogger(): Logger = AndroidLogger(debug = BuildConfig.DEBUG)

    @Provides
    @Singleton
    fun provideApiCreator(
        okHttpClient: OkHttpClient
    ): NetworkApiCreator {
        return NetworkApiCreator(okHttpClient, "https://placeholder.com")
    }

    @Provides
    @Singleton
    fun httpExceptionHandler(
        resourceManager: ResourceManager
    ): HttpExceptionHandler = HttpExceptionHandler(resourceManager)

    @Provides
    @Singleton
    fun provideSocketFactory() = WebSocketFactory()

    @Provides
    fun provideReconnector() = Reconnector()

    @Provides
    fun provideRequestExecutor() = RequestExecutor()

    @Provides
    fun provideSocketService(
        mapper: Gson,
        socketFactory: WebSocketFactory,
        logger: Logger,
        reconnector: Reconnector,
        requestExecutor: RequestExecutor
    ): SocketService = SocketService(mapper, logger, socketFactory, reconnector, requestExecutor)

    @Provides
    @Singleton
    fun provideSocketSingleRequestExecutor(
        mapper: Gson,
        logger: Logger,
        socketFactory: WebSocketFactory,
        resourceManager: ResourceManager
    ) = SocketSingleRequestExecutor(mapper, logger, socketFactory, resourceManager)

    @Provides
    @Singleton
    fun provideResourceManager(@ApplicationContext context: Context): ResourceManager {
        return ResourceManagerImpl(context)
    }

    @Provides
    @Singleton
    fun provideJsonMapper() = Gson()

}