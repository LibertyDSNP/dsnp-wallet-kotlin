package com.unfinished.dsnp_wallet_kotlin.ui.tabs

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.unfinished.dsnp_wallet_kotlin.R
import com.unfinished.dsnp_wallet_kotlin.databinding.FragmentProfileBinding
import com.unfinished.dsnp_wallet_kotlin.databinding.FragmentTabBinding
import com.unfinished.dsnp_wallet_kotlin.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TabFragment : BaseFragment<FragmentTabBinding>(FragmentTabBinding::inflate) {

    val TAG: String = "ProfileFragment"
    private val navController: NavController by lazy {
        val navHostFragment =
            parentFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navHostFragment.navController
    }

    override fun initialize() {

    }

    override fun observe() {

    }

}