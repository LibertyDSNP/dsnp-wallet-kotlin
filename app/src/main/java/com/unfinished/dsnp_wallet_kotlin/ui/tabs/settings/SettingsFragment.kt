package com.unfinished.dsnp_wallet_kotlin.ui.tabs.settings


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import com.unfinished.dsnp_wallet_kotlin.databinding.FragmentSettingsBinding
import com.unfinished.dsnp_wallet_kotlin.ui.onboarding.LandingViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.unfinished.common.base.BaseFragment
import com.unfinished.common.utils.setOnSafeClickListener

@AndroidEntryPoint
class SettingsFragment : BaseFragment<SettingsViewModel>() {

    val TAG: String = "SettingsFragment"
    lateinit var binding: FragmentSettingsBinding
    override val viewModel by viewModels<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initViews() {
         binding.settingsRecoveryPhrase.setOnSafeClickListener { viewModel.openMnemonicScreen() }
    }

    override fun subscribe(viewModel: SettingsViewModel) {

    }


}