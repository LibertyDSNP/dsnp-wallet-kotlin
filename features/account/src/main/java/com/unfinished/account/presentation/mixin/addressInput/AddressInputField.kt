package com.unfinished.account.presentation.mixin.addressInput

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import com.unfinished.common.R
import com.unfinished.account.databinding.ViewAddressInputBinding
import com.unfinished.common.utils.WithContextExtensions
import com.unfinished.common.utils.makeVisible
import com.unfinished.common.utils.setVisible
import com.unfinished.common.utils.useAttributes
import com.unfinished.common.view.shape.addRipple
import com.unfinished.common.view.shape.getInputBackground
import com.unfinished.common.view.shape.getRoundedCornerDrawable

class AddressInputField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : LinearLayout(context, attrs, defStyle), WithContextExtensions by WithContextExtensions(context) {

    lateinit var binding: ViewAddressInputBinding

    val content: EditText
        get() = binding.addressInputAddress

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL

        binding = ViewAddressInputBinding.inflate(LayoutInflater.from(context),this)

        setAddStatesFromChildren(true)

        setBackgrounds()

        attrs?.let(::applyAttributes)
    }

    fun setState(state: AddressInputState) {
        setIdenticonState(state.iconState)

        binding.addressInputScan.setVisible(state.scanShown)
        binding.addressInputPaste.setVisible(state.pasteShown)
        binding.addressInputClear.setVisible(state.clearShown)
        binding.addressInputMyself.setVisible(state.myselfShown)
    }

    fun onPasteClicked(listener: OnClickListener) {
        binding.addressInputPaste.setOnClickListener(listener)
    }

    fun onClearClicked(listener: OnClickListener) {
        binding.addressInputClear.setOnClickListener(listener)
    }

    fun onScanClicked(listener: OnClickListener) {
        binding.addressInputScan.setOnClickListener(listener)
    }

    fun onMyselfClicked(listener: OnClickListener) {
        binding.addressInputMyself.setOnClickListener(listener)
    }

    private fun setIdenticonState(state: AddressInputState.IdenticonState) {
        when (state) {
            is AddressInputState.IdenticonState.Address -> {
                binding.addressInputIdenticon.makeVisible()
                binding.addressInputIdenticon.setImageDrawable(state.drawable)
            }
            AddressInputState.IdenticonState.Placeholder -> {
                binding.addressInputIdenticon.makeVisible()
                binding.addressInputIdenticon.setImageResource(R.drawable.ic_identicon_placeholder)
            }
        }
    }

    private fun setBackgrounds() = with(context) {
        background = context.getInputBackground()

        binding.addressInputPaste.background = buttonBackground()
        binding.addressInputMyself.background = buttonBackground()
        binding.addressInputScan.background = buttonBackground()
    }

    private fun Context.buttonBackground() = addRipple(getRoundedCornerDrawable(R.color.button_background_secondary))

    private fun applyAttributes(attrs: AttributeSet) = context.useAttributes(attrs, com.unfinished.account.R.styleable.AddressInputField) {
        val hint = it.getString(com.unfinished.account.R.styleable.AddressInputField_android_hint)
        hint?.let { content.hint = hint }
    }
}
