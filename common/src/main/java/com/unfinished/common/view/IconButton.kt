package com.unfinished.common.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.unfinished.common.R
import com.unfinished.common.utils.WithContextExtensions
import com.unfinished.common.utils.setImageTintRes
import com.unfinished.common.utils.updatePadding
import com.unfinished.common.utils.useAttributes

class IconButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppCompatImageView(context, attrs, defStyleAttr), WithContextExtensions by WithContextExtensions(context) {

    init {
        updatePadding(top = 6.dp, bottom = 6.dp, start = 12.dp, end = 12.dp)

        setImageTintRes(R.color.chip_icon)

        background = addRipple(getRoundedCornerDrawable(R.color.button_background_secondary))

        attrs?.let(::applyAttributes)
    }

    fun setIcon(icon: Drawable) {
        setImageDrawable(icon)
    }

    private fun applyAttributes(attrs: AttributeSet) = context.useAttributes(attrs, R.styleable.IconButton) { typedArray ->
        val icon = typedArray.getDrawable(R.styleable.IconButton_android_src)
        icon?.let(::setIcon)
    }
}
