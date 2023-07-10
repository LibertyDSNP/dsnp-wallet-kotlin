package com.unfinished.feature_account.presentation.pincode.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import com.unfinished.feature_account.databinding.PincodeViewBinding

class PinCodeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    var binding: PincodeViewBinding

    var pinCodeEnteredListener: (String) -> Unit = {}
    var fingerprintClickListener: () -> Unit = {}

    private val pinCodeNumberClickListener = OnClickListener {
        pinNumberAdded((it as AppCompatButton).text.toString())
    }

    private val pinCodeDeleteClickListener = OnClickListener {
        deleteClicked()
    }

    private val pinCodeFingerprintClickListener = OnClickListener {
        fingerprintClickListener()
    }

    private var inputCode: String = ""

    private var progressView: DotsProgressView? = null

    init {
        binding = PincodeViewBinding.inflate(LayoutInflater.from(context), this)
        orientation = VERTICAL
        with(binding) {
            btn1.setOnClickListener(pinCodeNumberClickListener)
            btn2.setOnClickListener(pinCodeNumberClickListener)
            btn3.setOnClickListener(pinCodeNumberClickListener)
            btn4.setOnClickListener(pinCodeNumberClickListener)
            btn5.setOnClickListener(pinCodeNumberClickListener)
            btn6.setOnClickListener(pinCodeNumberClickListener)
            btn7.setOnClickListener(pinCodeNumberClickListener)
            btn8.setOnClickListener(pinCodeNumberClickListener)
            btn9.setOnClickListener(pinCodeNumberClickListener)
            btn0.setOnClickListener(pinCodeNumberClickListener)
            btnDelete.setOnClickListener(pinCodeDeleteClickListener)
            fingerprintBtn.setOnClickListener(pinCodeFingerprintClickListener)
        }
        updateProgress()
    }

    fun changeFingerPrintButtonVisibility(isVisible: Boolean) {
        if (isVisible) {
            binding.fingerprintBtn.visibility = View.VISIBLE
        } else {
            binding.fingerprintBtn.visibility = View.INVISIBLE
        }
    }

    fun resetInput() {
        inputCode = ""
        updateProgress()
    }

    fun bindProgressView(view: DotsProgressView) {
        progressView = view

        updateProgress()
    }

    fun pinCodeMatchingError() {
        resetInput()
        shakeDotsAnimation()
    }

    private fun pinNumberAdded(number: String) {
        if (inputCode.length >= DotsProgressView.MAX_PROGRESS) {
            return
        } else {
            inputCode += number
            updateProgress()
        }
        if (inputCode.length == DotsProgressView.MAX_PROGRESS) {
            pinCodeEnteredListener(inputCode)
        }
    }

    private fun deleteClicked() {
        if (inputCode.isEmpty()) {
            return
        }
        inputCode = inputCode.substring(0, inputCode.length - 1)
        updateProgress()
    }

    private fun updateProgress() {
        val currentProgress = inputCode.length
        progressView?.setProgress(currentProgress)
        binding.btnDelete.isEnabled = currentProgress != 0
    }

    private fun shakeDotsAnimation() {
        val animation = AnimationUtils.loadAnimation(context, com.unfinished.common.R.anim.shake)
        progressView?.startAnimation(animation)
    }
}
