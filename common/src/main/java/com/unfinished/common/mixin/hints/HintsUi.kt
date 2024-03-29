package com.unfinished.common.mixin.hints

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import com.unfinished.common.R
import com.unfinished.common.base.BaseFragment
import com.unfinished.common.utils.WithContextExtensions
import com.unfinished.common.utils.setDrawableStart
import com.unfinished.common.utils.setTextColorRes
import com.unfinished.common.utils.updatePadding

class HintsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr), WithContextExtensions by WithContextExtensions(context) {

    init {
        orientation = VERTICAL
    }

    fun setHints(hints: List<CharSequence>) {
        removeAllViews()

        hints.mapIndexed { index, hint ->
            TextView(context).apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setTextAppearance(R.style.TextAppearance_NovaFoundation_Regular_Caption1)
                }

                setTextColorRes(R.color.text_secondary)
                setDrawableStart(R.drawable.ic_nova, widthInDp = 16, paddingInDp = 8, tint = R.color.icon_secondary)

                text = hint

                if (index > 0) {
                    updatePadding(top = 8.dp)
                }
            }
        }.forEach(::addView)
    }
}

fun BaseFragment<*>.observeHints(mixin: HintsMixin, view: HintsView) {
    mixin.hintsFlow.observe(view::setHints)
}
