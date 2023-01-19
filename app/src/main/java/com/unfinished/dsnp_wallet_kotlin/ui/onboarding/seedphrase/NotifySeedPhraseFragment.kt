package com.unfinished.dsnp_wallet_kotlin.ui.onboarding.seedphrase

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.unfinished.dsnp_wallet_kotlin.R
import com.unfinished.dsnp_wallet_kotlin.databinding.FragmentNotifySeedPhraseBinding
import com.unfinished.dsnp_wallet_kotlin.databinding.FragmentVerifyCodeBinding
import com.unfinished.dsnp_wallet_kotlin.util.setOnSafeClickListener

enum class NotifySeedPhraseButton{
    CHECK_AGAIN,
    YES
}
class NotifySeedPhraseFragment : DialogFragment() {

    val TAG: String = "VerifyCodeFrag"
    lateinit var binding: FragmentNotifySeedPhraseBinding
    var closeFunction: ((NotifySeedPhraseButton) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotifySeedPhraseBinding.inflate(layoutInflater)
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