package com.unfinished.feature_account.presentation.export.seed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import io.novafoundation.nova.common.R
import com.unfinished.feature_account.databinding.FragmentExportSeedBinding
import com.unfinished.feature_account.presentation.export.ExportFragment
import com.unfinished.feature_account.presentation.export.ExportPayload
import com.unfinished.feature_account.presentation.mnemonic.confirm.ConfirmMnemonicViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.novafoundation.nova.common.view.shape.getRoundedCornerDrawable

private const val PAYLOAD_KEY = "PAYLOAD_KEY"

@AndroidEntryPoint
class ExportSeedFragment : ExportFragment<ExportSeedViewModel>() {

    override val viewModel by viewModels<ExportSeedViewModel>()
    lateinit var binding: FragmentExportSeedBinding

    companion object {
        fun getBundle(exportPayload: ExportPayload): Bundle {
            return Bundle().apply {
                putParcelable(PAYLOAD_KEY, exportPayload)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentExportSeedBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initViews() {
        viewModel.init(argument(PAYLOAD_KEY))

        binding.exportSeedToolbar.setHomeButtonListener { viewModel.back() }

        binding.exportSeedToolbar.setRightActionClickListener { viewModel.optionsClicked() }

        binding.exportSeedContentContainer.background = requireContext().getRoundedCornerDrawable(fillColorRes = R.color.input_background)
    }

    override fun subscribe(viewModel: ExportSeedViewModel) {
        super.subscribe(viewModel)

        viewModel.seedFlow.observe(binding.exportSeedValue::setText)
    }
}
