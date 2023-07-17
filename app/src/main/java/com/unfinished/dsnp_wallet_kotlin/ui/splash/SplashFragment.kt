package com.unfinished.dsnp_wallet_kotlin.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.unfinished.dsnp_wallet_kotlin.R
import dagger.hilt.android.AndroidEntryPoint
import com.unfinished.common.base.BaseFragment

@AndroidEntryPoint
class SplashFragment : BaseFragment<SplashViewModel>() {

    override val viewModel: SplashViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun initViews() {
    }


    override fun onDestroy() {
        super.onDestroy()

        (activity as? SplashBackgroundHolder)?.removeSplashBackground()
    }

    override fun subscribe(viewModel: SplashViewModel) {

    }

}
