package com.unfinished.common.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.unfinished.common.R
import com.unfinished.common.utils.WithContextExtensions
import com.unfinished.common.utils.getDrawableCompat
import com.unfinished.common.utils.makeVisible
import com.unfinished.common.utils.setVisible
import com.unfinished.common.view.shape.addRipple
import com.unfinished.common.view.shape.getCornersStateDrawable

class LabeledTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr), WithContextExtensions by WithContextExtensions(context) {

    private val view: View = View.inflate(context, R.layout.view_labeled_text, this)
    private val labeledTextAction: ImageView = view.findViewById(R.id.labeledTextAction)
    private val labeledTextIcon: ImageView = view.findViewById(R.id.labeledTextIcon)
    private val labeledTextLabel: TextView = view.findViewById(R.id.labeledTextLabel)
    private val labeledTextPrimaryIcon: ImageView = view.findViewById(R.id.labeledTextPrimaryIcon)
    private val labeledTextText: TextView = view.findViewById(R.id.labeledTextText)

    init {
        View.inflate(context, R.layout.view_labeled_text, this)

        with(context) {
            background = addRipple(getCornersStateDrawable())
        }

        applyAttributes(attrs)
    }

    private var singleLine: Boolean = true

    val textIconView: ImageView
        get() = labeledTextIcon

    val primaryIcon: ImageView
        get() = labeledTextPrimaryIcon

    private fun applyAttributes(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LabeledTextView)

            val label = typedArray.getString(R.styleable.LabeledTextView_label)
            label?.let(::setLabel)

            val message = typedArray.getString(R.styleable.LabeledTextView_message)
            message?.let(::setMessage)

            val messageStyle = typedArray.getResourceId(R.styleable.LabeledTextView_messageStyle, 0)
            if (messageStyle != 0) {
                labeledTextText.setTextAppearance(messageStyle)
            }

            val textIcon = typedArray.getDrawable(R.styleable.LabeledTextView_textIcon)
            textIcon?.let(::setTextIcon)

            val enabled = typedArray.getBoolean(R.styleable.LabeledTextView_enabled, true)
            isEnabled = enabled

            val actionIcon = typedArray.getDrawable(R.styleable.LabeledTextView_actionIcon)
            setActionIcon(actionIcon)

            singleLine = typedArray.getBoolean(R.styleable.LabeledTextView_android_singleLine, true)
            labeledTextText.isSingleLine = singleLine

            typedArray.recycle()
        }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)

        labeledTextAction.setVisible(enabled)
    }

    fun setLabel(label: String) {
        labeledTextLabel.text = label
    }

    fun setActionIcon(icon: Drawable?) {
        labeledTextAction.setImageDrawable(icon)

        labeledTextAction.setVisible(icon != null)
    }

    fun setMessage(@StringRes messageRes: Int) = setMessage(context.getString(messageRes))

    fun setMessage(text: String) {
        labeledTextText.text = text
    }

    fun setTextIcon(@DrawableRes iconRes: Int) = setTextIcon(context.getDrawableCompat(iconRes))

    fun setTextIcon(icon: Drawable) {
        labeledTextIcon.makeVisible()
        labeledTextIcon.setImageDrawable(icon)
    }

    fun setPrimaryIcon(icon: Drawable) {
        primaryIcon.makeVisible()
        primaryIcon.setImageDrawable(icon)
    }

    fun setActionClickListener(listener: (View) -> Unit) {
        labeledTextAction.setOnClickListener(listener)
    }

    fun setWholeClickListener(listener: (View) -> Unit) {
        setOnClickListener(listener)

        setActionClickListener(listener)
    }
}
