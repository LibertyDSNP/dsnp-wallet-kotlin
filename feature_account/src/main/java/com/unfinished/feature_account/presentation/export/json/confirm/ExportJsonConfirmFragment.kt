package com.unfinished.feature_account.presentation.export.json.confirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import coil.ImageLoader
import com.unfinished.feature_account.R
import com.unfinished.feature_account.databinding.FragmentExportJsonConfirmBinding
import com.unfinished.feature_account.presentation.export.ExportFragment
import com.unfinished.feature_account.presentation.mnemonic.confirm.ConfirmMnemonicViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.novafoundation.nova.common.di.FeatureUtils
import javax.inject.Inject

private const val PAYLOAD_KEY = "PAYLOAD_KEY"

@AndroidEntryPoint
class ExportJsonConfirmFragment : ExportFragment<ExportJsonConfirmViewModel>() {

    override val viewModel by viewModels<ExportJsonConfirmViewModel>()
    @Inject
    lateinit var imageLoader: ImageLoader
    lateinit var binding: FragmentExportJsonConfirmBinding

    companion object {
        fun getBundle(payload: ExportJsonConfirmPayload): Bundle {
            return Bundle().apply {
                putParcelable(PAYLOAD_KEY, payload)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentExportJsonConfirmBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initViews() {
        val payload = argument<ExportJsonConfirmPayload>(PAYLOAD_KEY)
        viewModel.init(payload)

        binding.exportJsonConfirmToolbar.setHomeButtonListener { viewModel.back() }

        binding.exportJsonConfirmToolbar.setRightActionClickListener { viewModel.optionsClicked() }

        binding.exportJsonConfirmExport.setOnClickListener { viewModel.confirmClicked() }
    }

    override fun subscribe(viewModel: ExportJsonConfirmViewModel) {
        super.subscribe(viewModel)
        binding.exportJsonConfirmValue.setMessage(viewModel.json)
    }
}
