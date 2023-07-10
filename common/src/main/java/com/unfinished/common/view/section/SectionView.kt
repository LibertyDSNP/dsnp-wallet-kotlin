package com.unfinished.common.view.section

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.unfinished.common.R
import com.unfinished.common.utils.WithContextExtensions
import com.unfinished.common.view.shape.addRipple
import com.unfinished.common.view.shape.getRoundedCornerDrawable

abstract class SectionView(
    layoutId: Int,
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int,
) : ConstraintLayout(context, attrs, defStyleAttr), WithContextExtensions {

    override val providedContext: Context = context

    init {
        View.inflate(context, layoutId, this)

        background = with(context) {
            addRipple(getRoundedCornerDrawable(R.color.block_background))
        }
    }
}
