package com.unfinished.dsnp_wallet_kotlin.ui.tabs.home

import android.os.Bundle
import com.unfinished.dsnp_wallet_kotlin.databinding.FragmentHomeBinding
import com.unfinished.dsnp_wallet_kotlin.ui.base.BaseFragment
import com.unfinished.feature_account.presentation.model.account.add.AddAccountPayload
import dagger.hilt.android.AndroidEntryPoint
import io.novafoundation.nova.common.resources.commonString
import io.novafoundation.nova.common.view.dialog.dialog

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    val TAG: String = "HomeFragment"

    companion object {
        private const val KEY_SKIP = "skip"
        private const val KEY_IDENTITY_SUCCESS = "identity_success"
        fun bundle(skip: Boolean, identitySuccess: Boolean): Bundle {
            return Bundle().apply {
                putBoolean(KEY_SKIP, skip)
                putBoolean(KEY_IDENTITY_SUCCESS, identitySuccess)
            }
        }
    }
    override fun initialize() {

    }

    override fun observe() {
        if (requireArguments().getBoolean(KEY_IDENTITY_SUCCESS)){
            showCreateIdentitySuccessDialog()
        }
        if (requireArguments().getBoolean(KEY_SKIP)){
            showSkipIdentityDialog()
        }
    }

    private fun showCreateIdentitySuccessDialog() {
        dialog(
            context = requireContext(),
            title = getString(commonString.congratulation),
            subtitle = getString(commonString.temp_handle),
            description = getString(commonString.temp_desc),
            buttonText = getString(commonString.lets_go),
            cancelable = false,
            primaryAction = {

            },
            secondaryAction = {

            },
            closeAction = {

            }
        ).show()
    }
    private fun showSkipIdentityDialog() {
        dialog(
            context = requireContext(),
            title = getString(commonString.warning),
            subtitle = getString(commonString.temp_handle),
            description = getString(commonString.temp_skip_desc),
            buttonText = getString(commonString.add_now),
            secondaryText = getString(commonString.skip),
            cancelable = false,
            primaryAction = {

            },
            secondaryAction = {

            },
            closeAction = {

            }
        ).show()
    }

}