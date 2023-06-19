package io.novafoundation.nova.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import io.novafoundation.nova.common.R
import io.novafoundation.nova.common.utils.WithContextExtensions
import io.novafoundation.nova.common.utils.useAttributes

class LinkView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : LinearLayout(context, attrs, defStyle),
    WithContextExtensions by WithContextExtensions(context) {

    private val view: View = View.inflate(context, R.layout.view_link, this)
    private val viewLinkText: TextView = view.findViewById(R.id.viewLinkText)

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL



        attrs?.let(::applyAttributes)
    }

    private fun applyAttributes(attributeSet: AttributeSet) =
        context.useAttributes(attributeSet, R.styleable.LinkView) {
            val linkText = it.getString(R.styleable.LinkView_linkText)
            viewLinkText.text = linkText
        }
}
