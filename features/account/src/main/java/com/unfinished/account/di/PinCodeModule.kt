package com.unfinished.account.di

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.Fragment
import com.unfinished.account.presentation.pincode.PincodeFragment
import com.unfinished.account.presentation.pincode.fingerprint.FingerprintCallback
import com.unfinished.account.presentation.pincode.fingerprint.FingerprintWrapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import com.unfinished.common.R
import com.unfinished.common.io.MainThreadExecutor
import com.unfinished.common.resources.ResourceManager

@Module
@InstallIn(FragmentComponent::class)
object PinCodeModule {

    @Provides
    fun provideCallback(fragment: Fragment) = fragment as PincodeFragment

    @Provides
    fun provideFingerprintWrapper(
        fragment: Fragment,
        @ApplicationContext context: Context,
        resourceManager: ResourceManager,
        fingerprintListener: FingerprintCallback
    ): FingerprintWrapper {
        val biometricManager = BiometricManager.from(context)
        val biometricPrompt = BiometricPrompt(fragment, MainThreadExecutor(), fingerprintListener)
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(resourceManager.getString(R.string.pincode_biometry_dialog_title))
            .setNegativeButtonText(resourceManager.getString(R.string.common_cancel))
            .build()

        return FingerprintWrapper(biometricManager, biometricPrompt, promptInfo)
    }

    @Provides
    fun provideFingerprintListener(fragment: Fragment): FingerprintCallback {
        return FingerprintCallback(fragment as PincodeFragment)
    }
}