package com.unfinished.common.view.tabs

import android.content.Context
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.widget.CompoundButton
import com.unfinished.common.R
import com.unfinished.common.utils.WithContextExtensions
import com.unfinished.common.view.shape.addRipple
import com.unfinished.common.view.shape.getCornersCheckableDrawable
import com.unfinished.common.view.shape.getRoundedCornerDrawable

class TabItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CompoundButton(ContextThemeWrapper(context, R.style.Widget_Nova_TabItem), attrs, defStyleAttr),
    WithContextExtensions by WithContextExtensions(context) {

    init {
        background = with(context) {
            getCornersCheckableDrawable(
                checked = addRipple(getRoundedCornerDrawable(fillColorRes = R.color.segmented_tab_active, cornerSizeInDp = 10), mask = getRippleMask(10)),
                unchecked = addRipple(
                    drawable = getRoundedCornerDrawable(fillColorRes = android.R.color.transparent, cornerSizeInDp = 10),
                    mask = getRippleMask(10)
                )
            )
        }
    }
}
