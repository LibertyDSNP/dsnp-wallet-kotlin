package com.unfinished.dsnp_wallet_kotlin.ui.onboarding


import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.DialogFragment
import com.unfinished.dsnp_wallet_kotlin.R
import com.unfinished.dsnp_wallet_kotlin.databinding.FragmentVerifyCodeBinding
import com.unfinished.dsnp_wallet_kotlin.util.hide
import com.unfinished.dsnp_wallet_kotlin.util.show
import com.unfinished.dsnp_wallet_kotlin.util.toast


class VerifyCodeFragment(private val inputText: String, private val icon: Int) : DialogFragment() {

    val TAG: String = "VerifyCodeFrag"
    lateinit var binding: FragmentVerifyCodeBinding
    var closeFunction: ((Boolean) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVerifyCodeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.sentTo.setText(inputText)
        binding.icon.setImageResource(icon)
        binding.cancel.setOnClickListener {
            this.dismiss()
        }
        binding.firstPinView.setAnimationEnable(true)
        binding.firstPinView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (count == 6){
                    if (s.toString().equals("000000")){
                        binding.error.show()
                    }else {
                        binding.error.hide()
                        toast(getString(R.string.temp_verify_success))
                        this@VerifyCodeFragment.dismiss()
                    }
                }
            }
        })
    }

    fun setDismissListener(function: ((Boolean) -> Unit)?) {
        closeFunction = function
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setGravity(Gravity.CENTER)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        closeFunction?.invoke(true)
    }
}