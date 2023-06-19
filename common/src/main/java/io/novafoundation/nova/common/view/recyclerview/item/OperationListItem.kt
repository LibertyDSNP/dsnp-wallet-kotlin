package io.novafoundation.nova.common.view.recyclerview.item

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.setPadding
import io.novafoundation.nova.common.R
import io.novafoundation.nova.common.utils.WithContextExtensions
import io.novafoundation.nova.common.utils.setImageTint
import io.novafoundation.nova.common.utils.setImageTintRes

class OperationListItem @kotlin.jvm.JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr),
    WithContextExtensions by WithContextExtensions(context) {

    enum class IconStyle {
        BORDERED_CIRCLE, DEFAULT
    }

    private val view: View = View.inflate(context, R.layout.item_operation_list_item, this)
    private val itemOperationHeader: TextView = view.findViewById(R.id.itemOperationHeader)
    private val itemOperationIcon: ImageView = view.findViewById(R.id.itemOperationIcon)
    private val itemOperationSubHeader: TextView = view.findViewById(R.id.itemOperationSubHeader)
    private val itemOperationValuePrimary: TextView =
        view.findViewById(R.id.itemOperationValuePrimary)
    private val itemOperationValueSecondary: TextView =
        view.findViewById(R.id.itemOperationValueSecondary)
    private val itemOperationValueStatus: ImageView =
        view.findViewById(R.id.itemOperationValueStatus)

    val icon: ImageView
        get() = itemOperationIcon

    val header: TextView
        get() = itemOperationHeader

    val subHeader: TextView
        get() = itemOperationSubHeader

    val valuePrimary: TextView
        get() = itemOperationValuePrimary

    val valueSecondary: TextView
        get() = itemOperationValueSecondary

    val status: ImageView
        get() = itemOperationValueStatus

    init {
        layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)

        setBackgroundResource(R.drawable.bg_primary_list_item)
    }

    fun setIconStyle(iconStyle: IconStyle) {
        when (iconStyle) {
            IconStyle.BORDERED_CIRCLE -> {
                icon.setPadding(6.dp)
                icon.setBackgroundResource(R.drawable.bg_icon_round_white)
                icon.setImageTintRes(R.color.icon_secondary)
            }

            IconStyle.DEFAULT -> {
                icon.setPadding(0)
                icon.background = null
                icon.setImageTint(null)
            }
        }
    }
}
