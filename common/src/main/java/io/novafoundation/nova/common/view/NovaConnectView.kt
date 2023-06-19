package io.novafoundation.nova.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import io.novafoundation.nova.common.R
import io.novafoundation.nova.common.utils.useAttributes

class NovaConnectView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : LinearLayout(context, attrs, defStyle) {

    private val view: View = View.inflate(context, R.layout.view_nova_connect, this)
    private val viewNovaConnectTargetIcon: ImageView =
        view.findViewById(R.id.viewNovaConnectTargetIcon)

    init {
        orientation = HORIZONTAL



        attrs?.let(::applyAttributes)
    }

    private fun applyAttributes(attributeSet: AttributeSet) =
        context.useAttributes(attributeSet, R.styleable.NovaConnectView) {
            val targetImage = it.getDrawable(R.styleable.NovaConnectView_targetImage)
            viewNovaConnectTargetIcon.setImageDrawable(targetImage)
        }
}
