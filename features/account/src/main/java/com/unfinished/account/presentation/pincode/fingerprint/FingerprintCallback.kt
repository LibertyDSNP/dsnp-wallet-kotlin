package com.unfinished.account.presentation.pincode.fingerprint

import android.annotation.SuppressLint
import androidx.biometric.BiometricPrompt
import com.unfinished.account.presentation.pincode.PincodeFragment

class FingerprintCallback(val fragment: PincodeFragment) :
    BiometricPrompt.AuthenticationCallback() {

    /**
     * These constants should be available per Google's doc, not sure why it is still throwing
     * this lint error.
     */
    @SuppressLint("RestrictedApi")
    override fun onAuthenticationError(errMsgId: Int, errString: CharSequence) {
        if (errMsgId != BiometricPrompt.ERROR_CANCELED &&
            errMsgId != BiometricPrompt.ERROR_NEGATIVE_BUTTON &&
            errMsgId != BiometricPrompt.ERROR_USER_CANCELED
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
