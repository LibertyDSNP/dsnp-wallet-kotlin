package com.unfinished.feature_account.presentation.handle.terms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.unfinished.feature_account.databinding.FragmentTermsHandleBinding
import dagger.hilt.android.AndroidEntryPoint
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.resources.commonString
import io.novafoundation.nova.common.view.dialog.dialog

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
        binding.termsHandleNext.setOnClickListener { viewModel.openHomeScreen(
            skip = false,
            identitySuccess = true
        ) }
    }

    override fun subscribe(viewModel: TermsHandleViewModel) {

    }

}
