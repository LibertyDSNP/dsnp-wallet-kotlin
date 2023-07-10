package com.unfinished.common.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.unfinished.common.R
import com.unfinished.common.utils.WithContextExtensions
import com.unfinished.common.utils.getColorFromAttr
import com.unfinished.common.utils.getEnum
import com.unfinished.common.utils.setTextOrHide
import com.unfinished.common.utils.useAttributes
import com.unfinished.common.view.shape.addRipple
import com.unfinished.common.view.shape.getRoundedCornerDrawableFromColors

class ButtonLarge @kotlin.jvm.JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr),
    WithContextExtensions by WithContextExtensions(context) {

    private val view: View = View.inflate(context, R.layout.button_large, this)
    private val buttonLargeIcon: ImageView = view.findViewById(R.id.buttonLargeIcon)
    private val buttonLargeSubtitle: TextView = view.findViewById(R.id.buttonLargeSubtitle)
    private val buttonLargeTitle: TextView = view.findViewById(R.id.buttonLargeTitle)

    init {


        minHeight = 52.dp

        attrs?.let(::applyAttributes)
    }

    enum class Style {
        PRIMARY,
        SECONDARY,
    }

    fun applyAttributes(attrs: AttributeSet) =
        context.useAttributes(attrs, R.styleable.ButtonLarge) {
            val style = it.getEnum(R.styleable.ButtonLarge_buttonLargeStyle, Style.PRIMARY)
            setStyle(style)

            val icon = it.getDrawable(R.styleable.ButtonLarge_icon)
            setIcon(icon)

            val title = it.getString(R.styleable.ButtonLarge_title)
            setTitle(title)

            val subtitle = it.getString(R.styleable.ButtonLarge_subTitle)
            setSubtitle(subtitle)
        }

    private fun setTitle(title: String?) {
        buttonLargeTitle.text = title
    }

    private fun setSubtitle(subtitle: String?) {
        buttonLargeSubtitle.setTextOrHide(subtitle)
    }

    private fun setIcon(icon: Drawable?) {
        buttonLargeIcon.setImageDrawable(icon)
    }

    private fun setStyle(style: Style) = with(context) {
        val backgroundColor = when (style) {
            Style.PRIMARY -> ContextCompat.getColor(context, R.color.button_background_primary)
            Style.SECONDARY -> ContextCompat.getColor(context, R.color.button_background_secondary)
        }

        val rippleColor = getColorFromAttr(androidx.appcompat.R.attr.colorControlHighlight)
        val baseBackground = context.getRoundedCornerDrawableFromColors(backgroundColor)

        background = addRipple(baseBackground, mask = null, rippleColor = rippleColor)
    }
}
