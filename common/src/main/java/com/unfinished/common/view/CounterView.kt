package com.unfinished.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import androidx.appcompat.widget.AppCompatTextView
import com.unfinished.common.R
import com.unfinished.common.view.shape.getRoundedCornerDrawable

class CounterView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(ContextThemeWrapper(context, R.style.Widget_Nova_Counter), attrs, defStyleAttr) {

    init {
        background = context.getRoundedCornerDrawable(R.color.chips_background, cornerSizeInDp = 8)
    }
}
