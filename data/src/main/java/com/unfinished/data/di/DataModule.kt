package com.unfinished.data.di

import android.content.Context
import android.content.SharedPreferences
import com.unfinished.data.file.FileProviderImpl
import com.unfinished.data.memory.ComputationalCache
import com.unfinished.data.memory.RealComputationalCache
import com.unfinished.data.secrets.v1.SecretStoreV1
import com.unfinished.data.secrets.v1.SecretStoreV1Impl
import com.unfinished.data.secrets.v2.SecretStoreV2
import com.unfinished.data.storage.Preferences
import com.unfinished.data.storage.PreferencesImpl
import com.unfinished.data.storage.encrypt.EncryptedPreferences
import com.unfinished.data.storage.encrypt.EncryptedPreferencesImpl
import com.unfinished.data.storage.encrypt.EncryptionUtil
import com.unfinished.interfaces.FileCache
import com.unfinished.data.file.FileProvider
import com.unfinished.interfaces.InternalFileSystemCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

const val SHARED_PREFERENCES_FILE = "fearless_prefs"

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

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
    fun provideFileProvider(@ApplicationContext context: Context): FileProvider {
        return FileProviderImpl(context)
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
    fun provideFileCache(fileProvider: FileProvider): FileCache = InternalFileSystemCache(fileProvider)

    @Provides
    @Singleton
    fun provideComputationalCache(): ComputationalCache = RealComputationalCache()

}