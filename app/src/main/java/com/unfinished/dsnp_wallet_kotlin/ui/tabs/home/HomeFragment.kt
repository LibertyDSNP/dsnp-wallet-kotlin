package com.unfinished.dsnp_wallet_kotlin.ui.tabs.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.unfinished.dsnp_wallet_kotlin.databinding.FragmentHomeBinding
import com.unfinished.dsnp_wallet_kotlin.ui.base.BaseFragment
import com.unfinished.dsnp_wallet_kotlin.util.createComposeView
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    val TAG: String = "HomeFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = requireContext().createComposeView {
        //IdentityScreen()
    }

    override fun initialize() {

    }

    override fun observe() {

    }


}