package com.unfinished.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.unfinished.common.utils.getEnum
import com.unfinished.common.utils.getResourceIdOrNull
import com.unfinished.common.utils.setTextColorRes
import com.unfinished.common.utils.useAttributes
import com.unfinished.common.view.shape.getRoundedCornerDrawable
import com.unfinished.common.R

class PlaceholderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {

    enum class Style(
        val showBackground: Boolean,
        val backgroundColorRes: Int?,
        val textColorRes: Int
    ) {
        BACKGROUND_PRIMARY(true, R.color.block_background, R.color.text_secondary),
        BACKGROUND_SECONDARY(true, R.color.block_background, R.color.text_secondary),
        NO_BACKGROUND(false, null, R.color.text_secondary)
    }

    private val view: View = View.inflate(context, R.layout.view_placeholder, this)
    private val viewPlaceholderImage: ImageView = view.findViewById(R.id.viewPlaceholderImage)
    private val viewPlaceholderText: TextView = view.findViewById(R.id.viewPlaceholderText)

    init {


        orientation = VERTICAL
        gravity = Gravity.CENTER_HORIZONTAL

        attrs?.let(::applyAttributes)
    }

    private fun applyAttributes(attrs: AttributeSet) =
        context.useAttributes(attrs, R.styleable.PlaceholderView) { typedArray ->
            val text = typedArray.getString(R.styleable.PlaceholderView_android_text)
            text?.let(::setText)

            val backgroundStyle = typedArray.getEnum(
                R.styleable.PlaceholderView_placeholderBackgroundStyle,
                Style.BACKGROUND_PRIMARY
            )
            setStyle(backgroundStyle)

            val image = typedArray.getResourceIdOrNull(R.styleable.PlaceholderView_image)
            image?.let(::setImage)
        }

    fun setStyle(style: Style) {
        background = if (style.showBackground) {
            context.getRoundedCornerDrawable(style.backgroundColorRes!!, cornerSizeInDp = 12)
        } else {
            null
        }
        viewPlaceholderText.setTextColorRes(style.textColorRes)
    }

    fun setImage(@DrawableRes image: Int) {
        viewPlaceholderImage.setImageResource(image)
    }

    fun setText(text: String) {
        viewPlaceholderText.text = text
    }
}
