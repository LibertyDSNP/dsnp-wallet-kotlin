package io.novafoundation.nova.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import io.novafoundation.nova.common.R
import io.novafoundation.nova.common.utils.WithContextExtensions
import io.novafoundation.nova.common.utils.getResourceIdOrNull
import io.novafoundation.nova.common.utils.onTextChanged
import io.novafoundation.nova.common.utils.setDrawableStart
import io.novafoundation.nova.common.utils.setVisible
import io.novafoundation.nova.common.utils.useAttributes

class SearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr), WithContextExtensions {

    override val providedContext: Context
        get() = context

    private val view: View = View.inflate(context, R.layout.view_search, this)
    private val searchClear: ImageView = view.findViewById(R.id.searchClear)
    private val searchContent: EditText = view.findViewById(R.id.searchContent)

    val content: EditText
        get() = searchContent

    init {


        background =
            getRoundedCornerDrawable(fillColorRes = R.color.input_background, cornerSizeDp = 10)

        orientation = HORIZONTAL

        content.onTextChanged {
            searchClear.setVisible(it.isNotEmpty())
        }
        searchClear.setOnClickListener {
            content.text.clear()
        }

        attrs?.let(::applyAttrs)
    }

    fun setHint(hint: String?) {
        content.hint = hint
    }

    fun setIcon(@DrawableRes icon: Int?) {
        searchContent.setDrawableStart(
            icon,
            widthInDp = 16,
            paddingInDp = 6,
            tint = R.color.icon_secondary
        )
    }

    private fun applyAttrs(attributeSet: AttributeSet) =
        context.useAttributes(attributeSet, R.styleable.SearchView) {
            val hint = it.getString(R.styleable.SearchView_android_hint)
            setHint(hint)

            val icon = it.getResourceIdOrNull(R.styleable.SearchView_icon)
            setIcon(icon)
        }
}
