package com.unfinished.feature_account.presentation.pincode.fingerprint

import androidx.biometric.BiometricConstants
import androidx.biometric.BiometricPrompt
import com.unfinished.feature_account.presentation.pincode.PinCodeViewModel
import com.unfinished.feature_account.presentation.pincode.PincodeFragment
import javax.inject.Inject

class FingerprintCallback(val fragment: PincodeFragment) : BiometricPrompt.AuthenticationCallback() {

    override fun onAuthenticationError(errMsgId: Int, errString: CharSequence) {
        if (errMsgId != BiometricConstants.ERROR_CANCELED &&
            errMsgId != BiometricConstants.ERROR_NEGATIVE_BUTTON &&
            errMsgId != BiometricConstants.ERROR_USER_CANCELED
        ) {
            fragment.viewModel.onAuthenticationError(errString.toString())
        }
    }

    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
        fragment.viewModel.biometryAuthenticationSucceeded()
    }

    override fun onAuthenticationFailed() {
        fragment.viewModel.biometryAuthenticationFailed()
    }
}
