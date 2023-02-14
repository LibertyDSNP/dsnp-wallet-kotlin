package com.unfinished.feature_account.presentation.pincode.fingerprint

import androidx.biometric.BiometricConstants
import androidx.biometric.BiometricPrompt
import com.unfinished.feature_account.presentation.pincode.PinCodeViewModel
import javax.inject.Inject

class FingerprintCallback : BiometricPrompt.AuthenticationCallback() {

    @Inject lateinit var pinCodeViewModel: PinCodeViewModel

    override fun onAuthenticationError(errMsgId: Int, errString: CharSequence) {
        if (errMsgId != BiometricConstants.ERROR_CANCELED &&
            errMsgId != BiometricConstants.ERROR_NEGATIVE_BUTTON &&
            errMsgId != BiometricConstants.ERROR_USER_CANCELED
        ) {
            pinCodeViewModel.onAuthenticationError(errString.toString())
        }
    }

    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
        pinCodeViewModel.biometryAuthenticationSucceeded()
    }

    override fun onAuthenticationFailed() {
        pinCodeViewModel.biometryAuthenticationFailed()
    }
}
