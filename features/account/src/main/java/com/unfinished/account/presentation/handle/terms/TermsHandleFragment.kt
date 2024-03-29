package com.unfinished.account.presentation.handle.terms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.unfinished.account.databinding.FragmentTermsHandleBinding
import dagger.hilt.android.AndroidEntryPoint
import com.unfinished.common.base.BaseFragment

@AndroidEntryPoint
class TermsHandleFragment : BaseFragment<TermsHandleViewModel>() {

    lateinit var binding: FragmentTermsHandleBinding
    override val viewModel: TermsHandleViewModel by viewModels()

    companion object {
        private const val KEY_HANDLE = "key_handle"
    }

    private val backCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTermsHandleBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initViews() {
        requireActivity().onBackPressedDispatcher.addCallback(this, backCallback)
        binding.termsHandleNext.setOnClickListener { viewModel.openTabScreen(
            skip = false,
            identitySuccess = true
        ) }
    }

    override fun subscribe(viewModel: TermsHandleViewModel) {

    }

}
