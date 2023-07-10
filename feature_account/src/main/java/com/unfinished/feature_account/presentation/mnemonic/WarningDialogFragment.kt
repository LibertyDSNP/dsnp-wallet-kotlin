package com.unfinished.feature_account.presentation.mnemonic

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.unfinished.feature_account.databinding.FragmentWarningDialogBinding
import dagger.hilt.android.AndroidEntryPoint
import com.unfinished.common.base.BaseDialogFragment
import com.unfinished.common.utils.setOnSafeClickListener

enum class WarningDialogButton{
    CHECK_AGAIN,
    YES
}

@AndroidEntryPoint
class WarningDialogFragment : BaseDialogFragment<SharedMnemonicViewModel>() {

    val TAG: String = "WarningMnemonicFrag"
    override val viewModel: SharedMnemonicViewModel by activityViewModels()
    lateinit var binding: FragmentWarningDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWarningDialogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initViews() {
        binding.checkAgain.setOnSafeClickListener {
            viewModel.invokeWarningButtonAction(WarningDialogButton.CHECK_AGAIN)
            this.dismiss()
        }
        binding.yes.setOnSafeClickListener {
            viewModel.invokeWarningButtonAction(WarningDialogButton.YES)
            this.dismiss()
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