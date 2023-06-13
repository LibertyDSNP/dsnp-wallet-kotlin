package com.unfinished.feature_account.presentation.handle.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.unfinished.feature_account.databinding.FragmentCreateHandleBinding
import com.unfinished.feature_account.presentation.pincode.fingerprint.FingerprintWrapper
import dagger.hilt.android.AndroidEntryPoint
import io.novafoundation.nova.common.base.BaseFragment
import javax.inject.Inject

@AndroidEntryPoint
class CreateHandleFragment : BaseFragment<CreateHandleViewModel>() {

    lateinit var binding: FragmentCreateHandleBinding
    override val viewModel: CreateHandleViewModel by viewModels()

    companion object {
        private const val KEY_HANDLE = "key_handle"
    }

    private val backCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCreateHandleBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initViews() {
        requireActivity().onBackPressedDispatcher.addCallback(this, backCallback)

    }

    override fun subscribe(viewModel: CreateHandleViewModel) {

    }

}
