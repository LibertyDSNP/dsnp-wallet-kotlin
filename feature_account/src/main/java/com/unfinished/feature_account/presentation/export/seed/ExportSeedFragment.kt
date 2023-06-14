package com.unfinished.feature_account.presentation.export.seed

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import io.novafoundation.nova.common.R
import com.unfinished.feature_account.databinding.FragmentExportSeedBinding
import com.unfinished.feature_account.presentation.export.ExportFragment
import com.unfinished.feature_account.presentation.export.ExportPayload
import com.unfinished.feature_account.presentation.mnemonic.backup.BackupMnemonicFragment
import com.unfinished.feature_account.presentation.mnemonic.backup.BackupMnemonicViewModel
import com.unfinished.feature_account.presentation.mnemonic.confirm.ConfirmMnemonicViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.novafoundation.nova.common.view.shape.getRoundedCornerDrawable
import javax.inject.Inject

private const val PAYLOAD_KEY = "PAYLOAD_KEY"

@AndroidEntryPoint
class ExportSeedFragment : ExportFragment<ExportSeedViewModel>() {

    lateinit var binding: FragmentExportSeedBinding
    @Inject
    lateinit var viewModelFactory: ExportSeedViewModel.AssistedFactory

    override val viewModel: ExportSeedViewModel by viewModels {
        ExportSeedViewModel.provideFactory(viewModelFactory,  argument(PAYLOAD_KEY))
    }

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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun subscribe(viewModel: ExportSeedViewModel) {
        super.subscribe(viewModel)
        viewModel.seedFlow.observe(binding.exportSeedValue::setText)
    }
}
