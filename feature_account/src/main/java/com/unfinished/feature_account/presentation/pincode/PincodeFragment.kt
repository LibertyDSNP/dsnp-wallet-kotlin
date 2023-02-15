package com.unfinished.feature_account.presentation.pincode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.novafoundation.nova.common.R
import com.unfinished.feature_account.databinding.FragmentPincodeBinding
import com.unfinished.feature_account.presentation.pincode.fingerprint.FingerprintWrapper
import dagger.hilt.android.AndroidEntryPoint
import io.novafoundation.nova.common.base.BaseFragment
import javax.inject.Inject

@AndroidEntryPoint
class PincodeFragment : BaseFragment<PinCodeViewModel>() {

    lateinit var binding: FragmentPincodeBinding
    @Inject
    lateinit var viewModelFactory: PinCodeViewModel.AssistedFactory

    override val viewModel: PinCodeViewModel by viewModels {
        PinCodeViewModel.provideFactory(viewModelFactory,  argument(PincodeFragment.KEY_PINCODE_ACTION))
    }

    companion object {
        private const val KEY_PINCODE_ACTION = "pincode_action"

        fun getPinCodeBundle(pinCodeAction: PinCodeAction): Bundle {
            return Bundle().apply {
                putParcelable(KEY_PINCODE_ACTION, pinCodeAction)
            }
        }
    }

    @Inject lateinit var fingerprintWrapper: FingerprintWrapper

    private val backCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            viewModel.backPressed()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPincodeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initViews() {
        requireActivity().onBackPressedDispatcher.addCallback(this, backCallback)

        binding.toolbar.setHomeButtonListener { viewModel.backPressed() }

        viewModel.fingerprintScannerAvailable(fingerprintWrapper.isAuthReady())

        with(binding.pinCodeNumbers) {
            pinCodeEnteredListener = { viewModel.pinCodeEntered(it) }
            fingerprintClickListener = { fingerprintWrapper.toggleScanner() }
        }

        binding.pinCodeNumbers.bindProgressView(binding.pincodeProgress)
    }

    override fun subscribe(viewModel: PinCodeViewModel) {
        /*
        viewModel.pinCodeAction.toolbarConfiguration.titleRes?.let {
            binding.toolbar.setTitle(getString(it))
        }
        */

        viewModel.startFingerprintScannerEventLiveData.observeEvent {
            if (fingerprintWrapper.isAuthReady()) {
                fingerprintWrapper.startAuth()
            }
        }

        viewModel.biometricSwitchDialogLiveData.observeEvent {
            showAuthWithBiometryDialog()
        }

        viewModel.showFingerPrintEvent.observeEvent {
            binding.pinCodeNumbers.changeFingerPrintButtonVisibility(fingerprintWrapper.isAuthReady())
        }

        viewModel.fingerPrintErrorEvent.observeEvent {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.homeButtonVisibilityLiveData.observe(binding.toolbar::setHomeButtonVisibility)

        viewModel.matchingPincodeErrorEvent.observeEvent {
            binding.pinCodeNumbers.pinCodeMatchingError()
        }

        viewModel.resetInputEvent.observeEvent {
            binding.pinCodeNumbers.resetInput()
            binding.pinCodeTitle.text = it
        }

        viewModel.startAuth()
    }

    private fun showAuthWithBiometryDialog() {
        MaterialAlertDialogBuilder(requireActivity(), R.style.AlertDialogTheme)
            .setTitle(R.string.pincode_biometry_dialog_title)
            .setMessage(R.string.pincode_fingerprint_switch_dialog_title)
            .setCancelable(false)
            .setPositiveButton(R.string.common_use) { _, _ ->
                viewModel.acceptAuthWithBiometry()
            }
            .setNegativeButton(R.string.common_skip) { _, _ ->
                viewModel.declineAuthWithBiometry()
            }
            .show()
    }

    override fun onPause() {
        super.onPause()
        fingerprintWrapper.cancel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

}
