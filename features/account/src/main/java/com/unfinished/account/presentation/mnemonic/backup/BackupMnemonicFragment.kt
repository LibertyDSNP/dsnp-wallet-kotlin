package com.unfinished.account.presentation.mnemonic.backup

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.unfinished.account.databinding.FragmentBackupMnemonicBinding
import com.unfinished.account.presentation.mnemonic.SharedMnemonicViewModel
import com.unfinished.account.presentation.mnemonic.WarningDialogButton
import com.unfinished.account.presentation.mnemonic.WarningDialogFragment
import com.unfinished.account.presentation.mnemonic.adapter.BackupWordAdapter
import dagger.hilt.android.AndroidEntryPoint
import com.unfinished.common.R
import com.unfinished.common.base.BaseFragment
import com.unfinished.common.utils.createSpannable
import com.unfinished.common.utils.setOnSafeClickListener
import javax.inject.Inject

@AndroidEntryPoint
class BackupMnemonicFragment : BaseFragment<BackupMnemonicViewModel>() {

    lateinit var binding: FragmentBackupMnemonicBinding
    val sharedViewModel: SharedMnemonicViewModel by activityViewModels()
    @Inject
    lateinit var viewModelFactory: BackupMnemonicViewModel.AssistedFactory
    override val viewModel: BackupMnemonicViewModel by viewModels {
        BackupMnemonicViewModel.provideFactory(viewModelFactory,  argument(KEY_ADD_ACCOUNT_PAYLOAD))
    }

    companion object {

        private const val KEY_ADD_ACCOUNT_PAYLOAD = "BackupMnemonicFragment.payload"

        fun getBundle(payload: BackupMnemonicPayload): Bundle {
            return Bundle().apply {
                putParcelable(KEY_ADD_ACCOUNT_PAYLOAD, payload)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBackupMnemonicBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initViews() {
        //binding.backupMnemonicToolbar.setHomeButtonListener { viewModel.homeButtonClicked() }
        //binding.backupMnemonicToolbar.setRightActionClickListener { viewModel.optionsClicked() }
        viewModel.warningAccepted()
        binding.subTitle.apply {
            linksClickable = false
            isClickable = false
            movementMethod = LinkMovementMethod.getInstance()
            text = createSpannable(
                content = getString(com.unfinished.account.R.string.seed_phrase_sub_title),
                typeface = ResourcesCompat.getFont(requireContext(), R.font.poppins_extrabold),
                highlightTextColor = R.color.orange
            )  {
                clickable(getString(com.unfinished.account.R.string.seed_phrase_sub_title_highlight)){}
            }
        }

        binding.seedPhraseBtn.setOnSafeClickListener {
            viewModel.showWarningDialog()
        }
    }

    override fun subscribe(viewModel: BackupMnemonicViewModel) {
        viewModel.continueText.observe(binding.seedPhraseBtn::setText)

        viewModel.showMnemonicWarningDialog.observeEvent {
            showMnemonicWarning()
        }

        sharedViewModel.warningDialogAction.observeEvent {
            when(it){
                WarningDialogButton.YES -> viewModel.nextClicked()
                WarningDialogButton.CHECK_AGAIN -> {}
            }
        }

        viewModel.mnemonicDisplay.observe {
            binding.seedPhraseRv.apply {
                layoutManager = GridLayoutManager(requireContext(),2)
                adapter = BackupWordAdapter(it.orEmpty())
            }
        }
    }

    private fun showMnemonicWarning() {
        val warningDialogFragment = WarningDialogFragment()
        warningDialogFragment.show(childFragmentManager,"warning_dialog")
    }
}
