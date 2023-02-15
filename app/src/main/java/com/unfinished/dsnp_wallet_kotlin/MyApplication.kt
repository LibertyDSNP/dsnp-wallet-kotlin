package com.unfinished.dsnp_wallet_kotlin

import android.app.Application
import android.app.Service
import android.content.ContentResolver
import android.content.Context
import android.content.res.Configuration
import coil.ImageLoader
import com.google.gson.Gson
import com.unfinished.dsnp_wallet_kotlin.util.ReleaseTree
import com.unfinished.feature_account.data.signer.SignerProvider
import dagger.hilt.android.HiltAndroidApp
import io.novafoundation.nova.common.address.AddressIconGenerator
import io.novafoundation.nova.common.address.CachingAddressIconGenerator
import io.novafoundation.nova.common.data.memory.ComputationalCache
import io.novafoundation.nova.common.data.network.AppLinksProvider
import io.novafoundation.nova.common.data.network.HttpExceptionHandler
import io.novafoundation.nova.common.data.network.NetworkApiCreator
import io.novafoundation.nova.common.data.network.rpc.BulkRetriever
import io.novafoundation.nova.common.data.network.rpc.SocketSingleRequestExecutor
import io.novafoundation.nova.common.data.secrets.v1.SecretStoreV1
import io.novafoundation.nova.common.data.secrets.v2.SecretStoreV2
import io.novafoundation.nova.common.data.storage.Preferences
import io.novafoundation.nova.common.data.storage.encrypt.EncryptedPreferences
import io.novafoundation.nova.common.di.CommonApi
import io.novafoundation.nova.common.di.modules.CommonModule_ProvideAppVersionsProviderFactory
import io.novafoundation.nova.common.di.modules.CommonModule_ProvideDefaultPagedKeysRetrieverFactory
import io.novafoundation.nova.common.interfaces.FileCache
import io.novafoundation.nova.common.interfaces.FileProvider
import io.novafoundation.nova.common.mixin.actionAwaitable.ActionAwaitableMixin
import io.novafoundation.nova.common.mixin.api.CustomDialogDisplayer
import io.novafoundation.nova.common.mixin.api.NetworkStateMixin
import io.novafoundation.nova.common.mixin.hints.ResourcesHintsMixinFactory
import io.novafoundation.nova.common.resources.*
import io.novafoundation.nova.common.utils.QrCodeGenerator
import io.novafoundation.nova.common.utils.bluetooth.BluetoothManager
import io.novafoundation.nova.common.utils.permissions.PermissionsAskerFactory
import io.novafoundation.nova.common.utils.systemCall.SystemCallExecutor
import io.novafoundation.nova.common.validation.ValidationExecutor
import io.novafoundation.nova.common.vibration.DeviceVibrator
import jp.co.soramitsu.fearless_utils.encrypt.Signer
import jp.co.soramitsu.fearless_utils.icon.IconGenerator
import jp.co.soramitsu.fearless_utils.wsrpc.SocketService
import jp.co.soramitsu.fearless_utils.wsrpc.logging.Logger
import okhttp3.OkHttpClient
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltAndroidApp
class MyApplication: Application(), CommonApi {

    @Inject lateinit var contextManager: ContextManager
    @Inject lateinit var computationalCache: ComputationalCache
    @Inject lateinit var imageloader: ImageLoader
    @Inject lateinit var resourceManager: ResourceManager
    @Inject lateinit var networkApiCreator: NetworkApiCreator
    @Inject lateinit var appLinksProvider: AppLinksProvider
    @Inject lateinit var preferences: Preferences
    @Inject lateinit var encryptedPreferences: EncryptedPreferences
    @Inject lateinit var iconGenerator: IconGenerator
    @Inject lateinit var clipboardManager: ClipboardManager
    @Inject lateinit var deviceVibrator: DeviceVibrator
    @Inject lateinit var signerProvider: Signer
    @Inject lateinit var logger: Logger
    @Inject lateinit var gson: Gson
    var languagesHolder: LanguagesHolder = LanguagesHolder()
    @Inject lateinit var socketService: SocketService
    @Inject lateinit var socketSingleRequestExecutor: SocketSingleRequestExecutor
    @Inject lateinit var addressIconGenerator: AddressIconGenerator
    @Inject lateinit var cachingAddressIconGenerator: AddressIconGenerator
    @Inject lateinit var networkStateMixin: NetworkStateMixin
    @Inject lateinit var qrCodeGenerator: QrCodeGenerator
    @Inject lateinit var fileProvider: FileProvider
    @Inject lateinit var random: Random
    @Inject lateinit var mContentResolver: ContentResolver
    @Inject lateinit var httpExceptionHandler: HttpExceptionHandler
    @Inject lateinit var builkRetriever: BulkRetriever
    @Inject lateinit var validationExecutor: ValidationExecutor
    @Inject lateinit var secretStoreV1: SecretStoreV1
    @Inject lateinit var secretStoreV2: SecretStoreV2
    @Inject lateinit var customDialogDisplayer: CustomDialogDisplayer.Presentation
    @Inject lateinit var appVersionsProvider: AppVersionProvider
    @Inject lateinit var systemCallExecutor: SystemCallExecutor
    @Inject lateinit var actionAwaitableMixinFactory: ActionAwaitableMixin.Factory
    @Inject lateinit var okHttpClient: OkHttpClient
    @Inject lateinit var fileCache: FileCache
    @Inject lateinit var bluetoothManager: BluetoothManager

    override fun attachBaseContext(base: Context) {
        val contextManager = ContextManager.getInstanceOrInit(base, languagesHolder)
        super.attachBaseContext(contextManager.setLocale(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val contextManager = ContextManager.getInstanceOrInit(this, languagesHolder)
        contextManager.setLocale(this)
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree());
        } else {
            Timber.plant(ReleaseTree())
        }
    }


    override fun computationalCache(): ComputationalCache = computationalCache

    override fun imageLoader(): ImageLoader = imageloader

    override fun provideResourceManager(): ResourceManager = resourceManager

    override fun provideNetworkApiCreator(): NetworkApiCreator = networkApiCreator

    override fun provideAppLinksProvider(): AppLinksProvider = appLinksProvider

    override fun providePreferences(): Preferences = preferences

    override fun provideEncryptedPreferences(): EncryptedPreferences = encryptedPreferences

    override fun provideIconGenerator(): IconGenerator = iconGenerator

    override fun provideClipboardManager(): ClipboardManager = clipboardManager

    override fun provideDeviceVibrator(): DeviceVibrator = deviceVibrator

    override fun signer(): Signer = signerProvider

    override fun logger(): Logger = logger

    override fun contextManager(): ContextManager = contextManager

    override fun languagesHolder(): LanguagesHolder = languagesHolder

    override fun provideJsonMapper(): Gson = gson

    override fun socketServiceCreator(): SocketService = socketService

    override fun provideSocketSingleRequestExecutor(): SocketSingleRequestExecutor = socketSingleRequestExecutor

    override fun addressIconGenerator(): AddressIconGenerator = addressIconGenerator

    override fun cachingAddressIconGenerator(): AddressIconGenerator = cachingAddressIconGenerator

    override fun networkStateMixin(): NetworkStateMixin = networkStateMixin

    override fun qrCodeGenerator(): QrCodeGenerator = qrCodeGenerator

    override fun fileProvider(): FileProvider = fileProvider

    override fun random(): Random = random

    override fun contentResolver(): ContentResolver  = mContentResolver

    override fun httpExceptionHandler(): HttpExceptionHandler = httpExceptionHandler

    override fun defaultPagedKeysRetriever(): BulkRetriever = builkRetriever

    override fun validationExecutor(): ValidationExecutor = validationExecutor

    override fun secretStoreV1(): SecretStoreV1 = secretStoreV1

    override fun secretStoreV2(): SecretStoreV2 = secretStoreV2

    override fun customDialogDisplayer(): CustomDialogDisplayer.Presentation = customDialogDisplayer

    override fun appVersionsProvider(): AppVersionProvider = appVersionsProvider
    override fun systemCallExecutor(): SystemCallExecutor = systemCallExecutor

    override fun actionAwaitableMixinFactory(): ActionAwaitableMixin.Factory = actionAwaitableMixinFactory

    override fun okHttpClient(): OkHttpClient = okHttpClient

    override fun fileCache(): FileCache = fileCache

    override fun bluetoothManager(): BluetoothManager = bluetoothManager


}