package com.unfinished.dsnp_wallet_kotlin.ui.tabs

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.unfinished.dsnp_wallet_kotlin.R
import com.unfinished.dsnp_wallet_kotlin.databinding.FragmentProfileBinding
import com.unfinished.dsnp_wallet_kotlin.databinding.FragmentTabBinding
import com.unfinished.dsnp_wallet_kotlin.ui.base.BaseFragment
import com.unfinished.dsnp_wallet_kotlin.ui.tabs.home.HomeFragment
import dagger.hilt.android.AndroidEntryPoint
import io.novafoundation.nova.common.resources.commonString
import io.novafoundation.nova.common.view.dialog.dialog

@AndroidEntryPoint
class TabFragment : BaseFragment<FragmentTabBinding>(FragmentTabBinding::inflate) {

    val TAG: String = "ProfileFragment"
    private val navController: NavController by lazy {
        val navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_tab_fragment) as NavHostFragment
        navHostFragment.navController
    }

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
           binding.bottomNavigationView.setupWithNavController(navController)
    }

    override fun observe() {
        arguments?.let {
            if (it.getBoolean(KEY_IDENTITY_SUCCESS)){
                showCreateIdentitySuccessDialog()
            }
            if (it.getBoolean(KEY_SKIP)){
                showSkipIdentityDialog()
            }
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