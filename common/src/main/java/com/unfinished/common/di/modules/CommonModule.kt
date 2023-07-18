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
import com.unfinished.common.mixin.actionAwaitable.ActionAwaitableMixin
import com.unfinished.common.mixin.actionAwaitable.ActionAwaitableProvider
import com.unfinished.common.mixin.api.CustomDialogDisplayer
import com.unfinished.common.mixin.api.NetworkStateMixin
import com.unfinished.common.mixin.hints.ResourcesHintsMixinFactory
import com.unfinished.common.mixin.impl.CustomDialogProvider
import com.unfinished.common.mixin.impl.NetworkStateProvider
import com.unfinished.common.pref.Preferences
import com.unfinished.common.pref.PreferencesImpl
import com.unfinished.common.resources.AppVersionProvider
import com.unfinished.common.resources.ClipboardManager
import com.unfinished.common.resources.ContextManager
import com.unfinished.common.resources.LanguagesHolder
import com.unfinished.common.resources.OSAppVersionProvider
import com.unfinished.common.resources.ResourceManager
import com.unfinished.common.resources.ResourceManagerImpl
import com.unfinished.common.utils.AppLinksProvider
import com.unfinished.common.utils.QrCodeGenerator
import com.unfinished.common.utils.permissions.PermissionsAskerFactory
import com.unfinished.common.utils.systemCall.SystemCallExecutor
import com.unfinished.common.validation.ValidationExecutor
import com.unfinished.common.vibration.DeviceVibrator
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
    fun providePreferences(sharedPreferences: SharedPreferences): Preferences {
        return PreferencesImpl(sharedPreferences)
    }

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
    fun provideValidationExecutor(): ValidationExecutor {
        return ValidationExecutor()
    }


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
    fun providePermissionAskerFactory(
        actionAwaitableMixinFactory: ActionAwaitableMixin.Factory
    ) = PermissionsAskerFactory(actionAwaitableMixinFactory)

    @Provides
    fun provideNetworkStateMixin(): NetworkStateMixin = NetworkStateProvider()

    @Provides
    @Singleton
    fun provideAppLinksProvider(): AppLinksProvider {
        return AppLinksProvider(
            termsUrl = com.unfinished.common.BuildConfig.TERMS_URL,
            privacyUrl = com.unfinished.common.BuildConfig.PRIVACY_URL,
            payoutsLearnMore = com.unfinished.common.BuildConfig.PAYOUTS_LEARN_MORE,
            twitterAccountTemplate = com.unfinished.common.BuildConfig.TWITTER_ACCOUNT_TEMPLATE,
            setControllerLearnMore = com.unfinished.common.BuildConfig.SET_CONTROLLER_LEARN_MORE,
            recommendedValidatorsLearnMore = com.unfinished.common.BuildConfig.RECOMMENDED_VALIDATORS_LEARN_MORE,
            paritySignerTroubleShooting = com.unfinished.common.BuildConfig.PARITY_SIGNER_TROUBLESHOOTING,
            ledgerBluetoothGuide = com.unfinished.common.BuildConfig.LEDGER_BLEUTOOTH_GUIDE,
            telegram = com.unfinished.common.BuildConfig.TELEGRAM_URL,
            twitter = com.unfinished.common.BuildConfig.TWITTER_URL,
            rateApp = com.unfinished.common.BuildConfig.RATE_URL,
            website = com.unfinished.common.BuildConfig.WEBSITE_URL,
            github = com.unfinished.common.BuildConfig.GITHUB_URL,
            email = com.unfinished.common.BuildConfig.EMAIL,
            youtube = com.unfinished.common.BuildConfig.YOUTUBE_URL,
        )
    }
}
