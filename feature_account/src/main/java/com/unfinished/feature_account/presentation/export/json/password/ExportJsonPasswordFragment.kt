package com.unfinished.feature_account.presentation.export.json.password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import com.unfinished.feature_account.databinding.FragmentExportJsonPasswordBinding
import com.unfinished.feature_account.presentation.export.ExportPayload
import com.unfinished.feature_account.presentation.mnemonic.confirm.ConfirmMnemonicViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.mixin.impl.observeValidations
import io.novafoundation.nova.common.utils.bindTo
import io.novafoundation.nova.common.view.setState
import javax.inject.Inject

private const val PAYLOAD_KEY = "PAYLOAD_KEY"

@AndroidEntryPoint
class ExportJsonPasswordFragment : BaseFragment<ExportJsonPasswordViewModel>() {

    override val viewModel by viewModels<ExportJsonPasswordViewModel>()
    @Inject
    lateinit var imageLoader: ImageLoader
    lateinit var binding: FragmentExportJsonPasswordBinding

    companion object {
        fun getBundle(exportPayload: ExportPayload): Bundle {
            return Bundle().apply {
                putParcelable(PAYLOAD_KEY, exportPayload)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentExportJsonPasswordBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initViews() {
        binding.exportJsonPasswordToolbar.setHomeButtonListener { viewModel.back() }

        binding.exportJsonPasswordNext.setOnClickListener { viewModel.nextClicked() }

        binding.exportJsonPasswordNext.prepareForProgress(viewLifecycleOwner)
    }

    override fun subscribe(viewModel: ExportJsonPasswordViewModel) {
        binding.exportJsonPasswordNewField.content.bindTo(viewModel.passwordFlow, lifecycleScope)
        binding.exportJsonPasswordConfirmField.content.bindTo(viewModel.passwordConfirmationFlow, lifecycleScope)

        viewModel.nextButtonState.observe(binding.exportJsonPasswordNext::setState)

        observeValidations(viewModel)
    }
}
