package com.unfinished.common.view.bottomSheet.list.fixed

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import com.unfinished.common.R
import com.unfinished.common.utils.dp
import com.unfinished.common.utils.inflateChild
import com.unfinished.common.utils.setDrawableEnd
import com.unfinished.common.utils.setDrawableStart
import com.unfinished.common.utils.setTextOrHide
import com.unfinished.common.view.bottomSheet.BaseBottomSheet

typealias ViewGetter<V> = FixedListBottomSheet.() -> V

abstract class FixedListBottomSheet(
    context: Context,
    private val onCancel: (() -> Unit)? = null,
    private val viewConfiguration: ViewConfiguration = ViewConfiguration.default()
) : BaseBottomSheet(context) {

    class ViewConfiguration(
        @LayoutRes val layout: Int,
        val container: ViewGetter<ViewGroup>,
        val title: ViewGetter<TextView>,
    ) {
        companion object {
            fun default() = ViewConfiguration(
                layout = R.layout.bottom_sheeet_fixed_list,
                container = { findViewById(R.id.fixedListSheetItemContainer)!! },
                title = { findViewById(R.id.fixedListSheetTitle)!! },
            )
        }
    }

    init {
        setContentView(viewConfiguration.layout)
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setOnCancelListener { onCancel?.invoke() }
    }

    final override fun setContentView(layoutResId: Int) {
        super.setContentView(layoutResId)
    }

    override fun setTitle(@StringRes titleRes: Int) {
        viewConfiguration.title(this).setText(titleRes)
    }

    override fun setTitle(title: CharSequence?) {
        viewConfiguration.title(this).setTextOrHide(title?.toString())
    }

    fun item(@LayoutRes layoutRes: Int, builder: (View) -> Unit) {
        val container = viewConfiguration.container(this)

        val view = container.inflateChild(layoutRes)

        builder.invoke(view)

        container.addView(view)
    }

    fun addItem(view: View) {
        val container = viewConfiguration.container(this)
        container.addView(view)
    }

    fun <T : View> item(view: T, builder: (T) -> Unit) {
        builder.invoke(view)

        viewConfiguration.container(this).addView(view)
    }

    fun getCommonPadding(): Int {
        return 16.dp(context)
    }
}

fun FixedListBottomSheet.item(
    @DrawableRes icon: Int,
    title: String,
    showArrow: Boolean = false,
    onClick: (View) -> Unit,
) {
    item(R.layout.item_sheet_iconic_label) { view ->
        view.findViewById<TextView>(R.id.itemExternalActionContent)?.apply {
            text = title

            val paddingInDp = 12
            setDrawableStart(
                drawableRes = icon,
                widthInDp = 24,
                tint = R.color.icon_primary,
                paddingInDp = 12
            )

            if (showArrow) {
                setDrawableEnd(
                    drawableRes = R.drawable.ic_chevron_right,
                    widthInDp = 24,
                    tint = R.color.icon_secondary,
                    paddingInDp = paddingInDp
                )
            }
        }

        view.setDismissingClickListener(onClick)
    }
}

fun FixedListBottomSheet.item(
    @DrawableRes icon: Int,
    @StringRes titleRes: Int,
    showArrow: Boolean = false,
    onClick: (View) -> Unit
) {
    item(icon, context.getString(titleRes), showArrow, onClick)
}
