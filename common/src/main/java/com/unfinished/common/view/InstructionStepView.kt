package com.unfinished.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.getResourceIdOrThrow
import com.unfinished.common.R
import com.unfinished.common.utils.useAttributes

class InstructionStepView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : LinearLayout(context, attrs, defStyle) {

    private val view: View = View.inflate(context, R.layout.view_instruction_step, this)
    private val instructionStepIndicator: TextView = view.findViewById(R.id.instructionStepIndicator)
    private val instructionStepText: TextView = view.findViewById(R.id.instructionStepText)

    init {
        orientation = HORIZONTAL



        attrs?.let(::applyAttributes)
    }

    private fun applyAttributes(attributeSet: AttributeSet) = context.useAttributes(attributeSet, R.styleable.InstructionStepView) {
        val stepNumber = it.getString(R.styleable.InstructionStepView_stepNumber)
        instructionStepIndicator.text = stepNumber

        // use getResourceId() instead of getString() since resources might contain spans which will be lost if getString() is used
        val stepText = it.getResourceIdOrThrow(R.styleable.InstructionStepView_stepText)
        instructionStepText.setText(stepText)
    }
}
