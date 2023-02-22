package com.unfinished.feature_account.presentation.importing

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import coil.ImageLoader
import com.unfinished.feature_account.databinding.FragmentImportAccountBinding
import com.unfinished.feature_account.presentation.model.account.add.ImportAccountPayload
import dagger.hilt.android.AndroidEntryPoint
import io.novafoundation.nova.common.base.BaseFragment
import com.unfinished.feature_account.presentation.importing.source.model.FileRequester
import com.unfinished.feature_account.presentation.importing.source.model.ImportSource
import com.unfinished.feature_account.presentation.importing.source.model.RequestCode
import javax.inject.Inject

@AndroidEntryPoint
class ImportAccountFragment : BaseFragment<ImportAccountViewModel>() {

    override val viewModel: ImportAccountViewModel by viewModels()
    lateinit var binding: FragmentImportAccountBinding
    @Inject
    lateinit var imageLoader: ImageLoader

    companion object {

        private const val PAYLOAD = "ImportAccountFragment.PAYLOAD"

        fun getBundle(payload: ImportAccountPayload): Bundle {
            return Bundle().apply {
                putParcelable(PAYLOAD, payload)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImportAccountBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initViews() {
        binding.importAccountToolbar.setRightActionClickListener {
            viewModel.optionsClicked()
        }
        binding.importAccountToolbar.setHomeButtonListener { viewModel.homeButtonClicked() }

        binding.importAccountContinue.setOnClickListener { viewModel.nextClicked() }
        binding.importAccountContinue.prepareForProgress(viewLifecycleOwner)
    }

    override fun subscribe(viewModel: ImportAccountViewModel) {
        binding.importAccountTitle.setText(viewModel.importSource.nameRes)

        val sourceView = viewModel.importSource.initializeView(viewModel, fragment = this)
        binding. importAccountSourceContainer.addView(sourceView)

        observeFeatures(viewModel.importSource)

        binding.importAccountToolbar.setRightIconVisible(viewModel.importSource.encryptionOptionsAvailable)

        viewModel.nextButtonState.observe(binding.importAccountContinue::setState)
    }

    private fun observeFeatures(source: ImportSource) {
        if (source is FileRequester) {
            source.chooseJsonFileEvent.observeEvent {
                openFilePicker(it)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        data?.let { viewModel.systemCallResultReceived(requestCode, it) }
    }

    private fun openFilePicker(it: RequestCode) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/json"
        startActivityForResult(intent, it)
    }
}
