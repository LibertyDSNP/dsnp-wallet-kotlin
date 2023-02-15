package com.unfinished.feature_account.presentation.mnemonic.backup

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.unfinished.feature_account.databinding.FragmentBackupMnemonicBinding
import com.unfinished.feature_account.presentation.mnemonic.NotifySeedPhraseButton
import com.unfinished.feature_account.presentation.mnemonic.NotifySeedPhraseFragment
import com.unfinished.feature_account.presentation.mnemonic.adapter.BackupWordAdapter
import com.unfinished.feature_account.presentation.mnemonic.confirm.ConfirmMnemonicViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.novafoundation.nova.common.R
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.common.utils.createSpannable
import io.novafoundation.nova.common.utils.setOnSafeClickListener
import io.novafoundation.nova.common.view.dialog.dialog
import javax.inject.Inject

@AndroidEntryPoint
class BackupMnemonicFragment : BaseFragment<BackupMnemonicViewModel>() {

    lateinit var binding: FragmentBackupMnemonicBinding

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

        binding.subTitle.apply {
            linksClickable = false
            isClickable = false
            movementMethod = LinkMovementMethod.getInstance()
            text = createSpannable(
                content = getString(com.unfinished.feature_account.R.string.seed_phrase_sub_title),
                typeface = ResourcesCompat.getFont(requireContext(), R.font.poppins_extrabold),
                highlightTextColor = ContextCompat.getColor(requireContext(), R.color.orange))  {
                clickable(getString(com.unfinished.feature_account.R.string.seed_phrase_sub_title_highlight)){}
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

        viewModel.mnemonicDisplay.observe {
            binding.seedPhraseRv.apply {
                layoutManager = GridLayoutManager(requireContext(),2)
                adapter = BackupWordAdapter(it.orEmpty())
            }
        }
    }

    private fun showMnemonicWarning() {
        val notifySeedPhraseFragment = NotifySeedPhraseFragment()
        notifySeedPhraseFragment.setDismissListener {
            when(it){
                NotifySeedPhraseButton.YES -> {
                    notifySeedPhraseFragment.dismiss()
                    viewModel.nextClicked()
                }
                NotifySeedPhraseButton.CHECK_AGAIN -> {
                    notifySeedPhraseFragment.dismiss()
                }
            }
        }
        notifySeedPhraseFragment.show(childFragmentManager,"notify_seed_phrase")
    }
}
