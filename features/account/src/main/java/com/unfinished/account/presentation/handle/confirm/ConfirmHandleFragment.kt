package com.unfinished.account.presentation.handle.confirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.unfinished.account.databinding.FragmentConfirmHandleBinding
import dagger.hilt.android.AndroidEntryPoint
import com.unfinished.common.base.BaseFragment
import javax.inject.Inject

@AndroidEntryPoint
class ConfirmHandleFragment : BaseFragment<ConfirmHandleViewModel>() {

    lateinit var binding: FragmentConfirmHandleBinding
    @Inject
    lateinit var viewModelFactory: ConfirmHandleViewModel.AssistedFactory
    override val viewModel: ConfirmHandleViewModel by viewModels {
        ConfirmHandleViewModel.provideFactory(viewModelFactory, argument(KEY_PAYLOAD))
    }

    companion object {
        private const val KEY_PAYLOAD = "confirm_payload"
        fun getBundle(payload: ConfirmHandlePayload): Bundle {
            return Bundle().apply {
                putParcelable(KEY_PAYLOAD, payload)
            }
        }
    }

    private val backCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConfirmHandleBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initViews() {
        requireActivity().onBackPressedDispatcher.addCallback(this, backCallback)
        binding.confirmHandleNext.setOnClickListener { viewModel.openTermsHandleScreen() }
    }

    override fun subscribe(viewModel: ConfirmHandleViewModel) {
        binding.confirmHandlePrefix.text = viewModel.handle
    }

}
