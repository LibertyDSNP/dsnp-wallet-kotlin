package com.unfinished.feature_account.presentation.export.seed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.novafoundation.nova.common.R
import com.unfinished.feature_account.databinding.FragmentExportSeedBinding
import com.unfinished.feature_account.presentation.export.ExportFragment
import com.unfinished.feature_account.presentation.export.ExportPayload
import io.novafoundation.nova.common.view.shape.getRoundedCornerDrawable
import io.novafoundation.nova.feature_account_impl.presentation.exporting.seed.ExportSeedViewModel

private const val PAYLOAD_KEY = "PAYLOAD_KEY"

class ExportSeedFragment : ExportFragment<ExportSeedViewModel>() {

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
        binding.exportSeedToolbar.setHomeButtonListener { viewModel.back() }

        binding.exportSeedToolbar.setRightActionClickListener { viewModel.optionsClicked() }

        binding.exportSeedContentContainer.background = requireContext().getRoundedCornerDrawable(fillColorRes = R.color.input_background)
    }

    override fun subscribe(viewModel: ExportSeedViewModel) {
        super.subscribe(viewModel)

        viewModel.seedFlow.observe(binding.exportSeedValue::setText)
    }
}
