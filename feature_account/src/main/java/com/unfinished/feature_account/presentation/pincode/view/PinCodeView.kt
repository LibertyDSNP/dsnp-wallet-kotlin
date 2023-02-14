package com.unfinished.feature_account.presentation.pincode.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import com.unfinished.feature_account.R

class PinCodeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

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

    private var btn1: AppCompatButton
    private var btn2: AppCompatButton
    private var btn3: AppCompatButton
    private var btn4: AppCompatButton
    private var btn5: AppCompatButton
    private var btn6: AppCompatButton
    private var btn7: AppCompatButton
    private var btn8: AppCompatButton
    private var btn9: AppCompatButton
    private var btn0: AppCompatButton
    private var btnDelete: AppCompatImageButton
    private var fingerprintBtn: AppCompatImageButton

    init {
        View.inflate(context, R.layout.pincode_view, this)

        orientation = VERTICAL
        btn1 = findViewById<AppCompatButton>(R.id.btn1)
        btn2 = findViewById<AppCompatButton>(R.id.btn2)
        btn3 = findViewById<AppCompatButton>(R.id.btn3)
        btn4 = findViewById<AppCompatButton>(R.id.btn4)
        btn5 = findViewById<AppCompatButton>(R.id.btn5)
        btn6 = findViewById<AppCompatButton>(R.id.btn6)
        btn7 = findViewById<AppCompatButton>(R.id.btn7)
        btn8 = findViewById<AppCompatButton>(R.id.btn8)
        btn9 = findViewById<AppCompatButton>(R.id.btn9)
        btn0 = findViewById<AppCompatButton>(R.id.btn0)
        btnDelete = findViewById<AppCompatImageButton>(R.id.btnDelete)
        fingerprintBtn = findViewById<AppCompatImageButton>(R.id.fingerprintBtn)
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

        updateProgress()
    }

    fun changeFingerPrintButtonVisibility(isVisible: Boolean) {
        if (isVisible) {
            fingerprintBtn.visibility = View.VISIBLE
        } else {
            fingerprintBtn.visibility = View.INVISIBLE
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

        btnDelete.isEnabled = currentProgress != 0
    }

    private fun shakeDotsAnimation() {
        val animation = AnimationUtils.loadAnimation(context, io.novafoundation.nova.common.R.anim.shake)
        progressView?.startAnimation(animation)
    }
}
