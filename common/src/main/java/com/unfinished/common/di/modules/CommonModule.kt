package com.unfinished.common.di.modules

import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Vibrator
import coil.ImageLoader
import coil.decode.SvgDecoder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.unfinished.common.address.AddressIconGenerator
import com.unfinished.common.address.CachingAddressIconGenerator
import com.unfinished.common.address.StatelessAddressIconGenerator
import com.unfinished.common.data.FileProviderImpl
import com.unfinished.common.data.memory.ComputationalCache
import com.unfinished.common.data.memory.RealComputationalCache
import com.unfinished.common.data.network.rpc.BulkRetriever
import com.unfinished.common.data.secrets.v1.SecretStoreV1
import com.unfinished.common.data.secrets.v1.SecretStoreV1Impl
import com.unfinished.common.data.secrets.v2.SecretStoreV2
import com.unfinished.common.data.storage.Preferences
import com.unfinished.common.data.storage.PreferencesImpl
import com.unfinished.common.data.storage.encrypt.EncryptedPreferences
import com.unfinished.common.data.storage.encrypt.EncryptedPreferencesImpl
import com.unfinished.common.data.storage.encrypt.EncryptionUtil
import com.unfinished.common.interfaces.FileCache
import com.unfinished.common.interfaces.FileProvider
import com.unfinished.common.interfaces.InternalFileSystemCache
import com.unfinished.common.mixin.actionAwaitable.ActionAwaitableMixin
import com.unfinished.common.mixin.actionAwaitable.ActionAwaitableProvider
import com.unfinished.common.mixin.api.CustomDialogDisplayer
import com.unfinished.common.mixin.hints.ResourcesHintsMixinFactory
import com.unfinished.common.mixin.impl.CustomDialogProvider
import com.unfinished.common.resources.AppVersionProvider
import com.unfinished.common.resources.ClipboardManager
import com.unfinished.common.resources.ContextManager
import com.unfinished.common.resources.LanguagesHolder
import com.unfinished.common.resources.OSAppVersionProvider
import com.unfinished.common.resources.ResourceManager
import com.unfinished.common.resources.ResourceManagerImpl
import com.unfinished.common.utils.QrCodeGenerator
import com.unfinished.common.utils.permissions.PermissionsAskerFactory
import com.unfinished.common.utils.systemCall.SystemCallExecutor
import com.unfinished.common.validation.ValidationExecutor
import com.unfinished.common.vibration.DeviceVibrator
import jp.co.soramitsu.fearless_utils.encrypt.Signer
import jp.co.soramitsu.fearless_utils.icon.IconGenerator
import java.security.SecureRandom
import java.util.Random
import javax.inject.Qualifier
import javax.inject.Singleton

const val SHARED_PREFERENCES_FILE = "fearless_prefs"

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Caching

@Module
@InstallIn(SingletonComponent::class)
class CommonModule {

    @Provides
    @Singleton
    fun provideComputationalCache(): ComputationalCache = RealComputationalCache()

    @Provides
    @Singleton
    fun imageLoader(@ApplicationContext context: Context) = ImageLoader.Builder(context)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()

    @Provides
    @Singleton
    fun provideResourceManager(contextManager: ContextManager): ResourceManager {
        return ResourceManagerImpl(contextManager)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun providePreferences(sharedPreferences: SharedPreferences): Preferences {
        return PreferencesImpl(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideEncryptionUtil(@ApplicationContext context: Context): EncryptionUtil {
        return EncryptionUtil(context)
    }

    @Provides
    @Singleton
    fun provideEncryptedPreferences(
        preferences: Preferences,
        encryptionUtil: EncryptionUtil,
    ): EncryptedPreferences {
        return EncryptedPreferencesImpl(preferences, encryptionUtil)
    }

    @Provides
    @Singleton
    fun provideSigner(): Signer {
        return Signer
    }

    @Provides
    @Singleton
    fun provideIconGenerator(): IconGenerator {
        return IconGenerator()
    }

    @Provides
    @Singleton
    fun provideClipboardManager(@ApplicationContext context: Context): ClipboardManager {
        return ClipboardManager(context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager)
    }

    @Provides
    @Singleton
    fun provideDeviceVibrator(@ApplicationContext context: Context): DeviceVibrator {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        return DeviceVibrator(vibrator)
    }

    @Provides
    @Singleton
    fun provideLanguagesHolder(): LanguagesHolder {
        return LanguagesHolder()
    }

    @Provides
    @Singleton
    fun provideAddressModelCreator(
        resourceManager: ResourceManager,
        iconGenerator: IconGenerator,
    ): AddressIconGenerator = StatelessAddressIconGenerator(iconGenerator, resourceManager)

    @Provides
    @Caching
    fun provideCachingAddressModelCreator(
        delegate: AddressIconGenerator,
    ): AddressIconGenerator = CachingAddressIconGenerator(delegate)

    @Provides
    @Singleton
    fun provideQrCodeGenerator(): QrCodeGenerator {
        return QrCodeGenerator(Color.BLACK, Color.WHITE)
    }

    @Provides
    @Singleton
    fun provideContextManager(
        @ApplicationContext context: Context,
        languagesHolder: LanguagesHolder
    ): ContextManager {
        return ContextManager(context,languagesHolder)
    }

    @Provides
    @Singleton
    fun provideFileProvider(contextManager: ContextManager): FileProvider {
        return FileProviderImpl(contextManager.getApplicationContext())
    }

    @Provides
    @Singleton
    fun provideRandom(): Random = SecureRandom()

    @Provides
    @Singleton
    fun provideContentResolver(
        @ApplicationContext context: Context,
    ): ContentResolver {
        return context.contentResolver
    }

    @Provides
    @Singleton
    fun provideDefaultPagedKeysRetriever(): BulkRetriever {
        return BulkRetriever()
    }

    @Provides
    @Singleton
    fun provideValidationExecutor(): ValidationExecutor {
        return ValidationExecutor()
    }

    @Provides
    @Singleton
    fun provideSecretStoreV1(
        encryptedPreferences: EncryptedPreferences,
    ): SecretStoreV1 = SecretStoreV1Impl(encryptedPreferences)

    @Provides
    @Singleton
    fun provideSecretStoreV2(
        encryptedPreferences: EncryptedPreferences,
    ) = SecretStoreV2(encryptedPreferences)

    @Provides
    @Singleton
    fun provideCustomDialogDisplayer(): CustomDialogDisplayer.Presentation = CustomDialogProvider()

    @Provides
    @Singleton
    fun provideAppVersionsProvider(@ApplicationContext context: Context): AppVersionProvider {
        return OSAppVersionProvider(context)
    }

    @Provides
    @Singleton
    fun provideSystemCallExecutor(
        contextManager: ContextManager
    ): SystemCallExecutor = SystemCallExecutor(contextManager)

    @Provides
    @Singleton
    fun actionAwaitableMixinFactory(): ActionAwaitableMixin.Factory = ActionAwaitableProvider

    @Provides
    @Singleton
    fun resourcesHintsMixinFactory(
        resourceManager: ResourceManager,
    ) = ResourcesHintsMixinFactory(resourceManager)

    @Provides
    @Singleton
    fun provideFileCache(fileProvider: FileProvider): FileCache = InternalFileSystemCache(fileProvider)

    @Provides
    @Singleton
    fun providePermissionAskerFactory(
        actionAwaitableMixinFactory: ActionAwaitableMixin.Factory
    ) = PermissionsAskerFactory(actionAwaitableMixinFactory)

}
