package com.unfinished.feature_account.presentation.mnemonic.confirm

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.unfinished.feature_account.databinding.FragmentCongratulationDialogBinding
import com.unfinished.feature_account.presentation.mnemonic.SharedMnemonicViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.novafoundation.nova.common.base.BaseDialogFragment
import io.novafoundation.nova.common.utils.setOnSafeClickListener

enum class CongratulationDialogButton{
    CREATE_PINCODE,
    SKIP
}

@AndroidEntryPoint
class CongratulationFragment : BaseDialogFragment<SharedMnemonicViewModel>() {

    lateinit var binding: FragmentCongratulationDialogBinding
    override val viewModel: SharedMnemonicViewModel by activityViewModels()

    companion object {
        private const val KEY_PAYLOAD = "confirm_payload"

        fun getBundle(payload: ConfirmMnemonicPayload): Bundle {
            return Bundle().apply {
                putParcelable(KEY_PAYLOAD, payload)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCongratulationDialogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initViews() {
        binding.createPincode.setOnSafeClickListener {
            viewModel.invokeCongButtonAction(CongratulationDialogButton.CREATE_PINCODE)
            dismiss()
        }
        binding.skip.setOnSafeClickListener {
            viewModel.invokeCongButtonAction(CongratulationDialogButton.SKIP)
            dismiss()
        }
    }

    override fun subscribe(viewModel: SharedMnemonicViewModel) {}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setGravity(Gravity.CENTER)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

}
