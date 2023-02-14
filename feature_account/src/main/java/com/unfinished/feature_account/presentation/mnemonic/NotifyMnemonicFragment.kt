package com.unfinished.feature_account.presentation.mnemonic

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.unfinished.feature_account.databinding.FragmentNotifyMnemonicBinding
import dagger.hilt.android.AndroidEntryPoint
import io.novafoundation.nova.common.utils.setOnSafeClickListener

enum class NotifySeedPhraseButton{
    CHECK_AGAIN,
    YES
}
@AndroidEntryPoint
class NotifySeedPhraseFragment : DialogFragment() {

    val TAG: String = "VerifyCodeFrag"
    lateinit var binding: FragmentNotifyMnemonicBinding
    var closeFunction: ((NotifySeedPhraseButton) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotifyMnemonicBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.checkAgain.setOnSafeClickListener {
            closeFunction?.invoke(NotifySeedPhraseButton.CHECK_AGAIN)
        }
        binding.yes.setOnSafeClickListener {
            closeFunction?.invoke(NotifySeedPhraseButton.YES)
        }
    }

    fun setDismissListener(function: ((NotifySeedPhraseButton) -> Unit)?) {
        closeFunction = function
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setGravity(Gravity.CENTER)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

}