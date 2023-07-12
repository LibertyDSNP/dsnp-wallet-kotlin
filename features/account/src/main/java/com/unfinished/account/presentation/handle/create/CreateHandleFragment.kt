package com.unfinished.account.presentation.handle.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.unfinished.account.databinding.FragmentCreateHandleBinding
import dagger.hilt.android.AndroidEntryPoint
import com.unfinished.common.base.BaseFragment
import com.unfinished.common.utils.showSoftKeyboard
import kotlinx.coroutines.flow.collectLatest

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCreateHandleBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initViews() {
        requireActivity().onBackPressedDispatcher.addCallback(this, backCallback)
        binding.inputField.showSoftKeyboard()
        binding.inputField.doOnTextChanged { text, _, _, _ ->
            viewModel.validateHandle(text.toString())
        }
        binding.createHandleNext.setOnClickListener {
            viewModel.openConfirmHandleScreen(binding.inputField.text.toString())
        }
        binding.createHandleSkip.setOnClickListener {
            viewModel.openTabScreen(skip = true, identitySuccess = false)
        }
    }

    override fun subscribe(viewModel: CreateHandleViewModel) {
        lifecycleScope.launchWhenResumed {
            viewModel.handleValidation.collectLatest { binding.createHandleNext.setState(it) }
        }
    }

}
