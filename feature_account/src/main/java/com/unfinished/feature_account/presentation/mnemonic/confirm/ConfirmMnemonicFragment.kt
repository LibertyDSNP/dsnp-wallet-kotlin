package com.unfinished.feature_account.presentation.mnemonic.confirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.unfinished.feature_account.databinding.FragmentConfirmMnemonicBinding
import com.unfinished.feature_account.presentation.mnemonic.SharedMnemonicViewModel
import com.unfinished.feature_account.presentation.mnemonic.adapter.DestinationWordAdapter
import com.unfinished.feature_account.presentation.mnemonic.adapter.SourceWordAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.utils.ItemOffsetDecoration
import io.novafoundation.nova.common.utils.setOnSafeClickListener
import javax.inject.Inject
import io.novafoundation.nova.common.R as commonR

@AndroidEntryPoint
class ConfirmMnemonicFragment : BaseFragment<ConfirmMnemonicViewModel>() {

    lateinit var binding: FragmentConfirmMnemonicBinding
    val sharedViewModel: SharedMnemonicViewModel by activityViewModels()
    @Inject
    lateinit var viewModelFactory: ConfirmMnemonicViewModel.AssistedFactory
    override val viewModel: ConfirmMnemonicViewModel by viewModels {
        ConfirmMnemonicViewModel.provideFactory(viewModelFactory,  argument(KEY_PAYLOAD))
    }

    val destinationAdapter: DestinationWordAdapter by lazy {
        DestinationWordAdapter(
            onItemClicked = { pos, destinationWord ->
                viewModel.destinationWordClicked(destinationWord)
            }
        )
    }

    val sourceAdapter: SourceWordAdapter by lazy {
        SourceWordAdapter(
            onItemClicked = { pos, sourceWord ->
                viewModel.sourceWordClicked(sourceWord)
            }
        )
    }

    companion object {
        private const val KEY_PAYLOAD = "confirm_payload"

        fun getBundle(payload: ConfirmMnemonicPayload): Bundle {
            return Bundle().apply {
                putParcelable(KEY_PAYLOAD, payload)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentConfirmMnemonicBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initViews() {
        //binding.confirmMnemonicToolbar.setHomeButtonListener { viewModel.homeButtonClicked() }
        //binding.confirmMnemonicToolbar.setRightActionClickListener { viewModel.reset() }
        //binding.conformMnemonicSkip.setOnClickListener { viewModel.skipClicked() }

        binding.confirmSeedPhraseRv.apply {
            layoutManager = GridLayoutManager(requireContext(),2)
            addItemDecoration(ItemOffsetDecoration(context, commonR.dimen.item_offset_vertical ,commonR.dimen.item_offset_horizontal))
            adapter = destinationAdapter
        }
        binding.confirmSelectSeedPhraseRv.apply {
            layoutManager = GridLayoutManager(requireContext(),3)
            addItemDecoration(ItemOffsetDecoration(context, commonR.dimen.item_offset_vertical,commonR.dimen.item_offset_vertical))
            adapter = sourceAdapter
        }

        binding.confirmSeedPhraseBtn.setOnSafeClickListener {
            viewModel.continueClicked()
        }

    }

    override fun subscribe(viewModel: ConfirmMnemonicViewModel) {
        //binding.conformMnemonicSkip.setVisible(viewModel.skipVisible)
        viewModel.nextButtonEnabled.observe {
            binding.confirmSeedPhraseBtn.isEnabled = it
        }

        viewModel.sourceWords.observe {
            sourceAdapter.updateList(it)
        }

        viewModel.destinationWords.observe {
            destinationAdapter.updateList(it)
        }

        viewModel.showCongDialogEvent.observeEvent {
            showCongratulationDialog()
        }

        sharedViewModel.congDialogAction.observeEvent {
            when(it){
                CongratulationDialogButton.CREATE_PINCODE -> viewModel.openPinCodeScreen()
                CongratulationDialogButton.SKIP -> {}
            }
        }
        viewModel.matchingMnemonicErrorAnimationEvent.observeEvent {

        }
    }

    private fun showCongratulationDialog() {
        val congratulationFragment = CongratulationFragment()
        congratulationFragment.show(childFragmentManager,"congratulation_dialog")
    }


}
