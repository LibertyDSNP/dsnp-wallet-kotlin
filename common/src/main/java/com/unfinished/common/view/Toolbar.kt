package com.unfinished.common.view

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.unfinished.common.R
import com.unfinished.common.utils.dp
import com.unfinished.common.utils.getResourceIdOrNull
import com.unfinished.common.utils.makeGone
import com.unfinished.common.utils.makeVisible
import com.unfinished.common.utils.setVisible

class Toolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val view: View = View.inflate(context, R.layout.view_toolbar, this)
    private val backImg: ImageView = view.findViewById(R.id.backImg)
    private val rightImg: ImageView = view.findViewById(R.id.rightImg)
    private val rightText: TextView = view.findViewById(R.id.rightText)
    private val titleTv: TextView = view.findViewById(R.id.titleTv)
    private val toolbarContainer: FrameLayout = view.findViewById(R.id.toolbarContainer)
    private val toolbarCustomActions: LinearLayout = view.findViewById(R.id.toolbarCustomActions)
    private val toolbarDivider: View = view.findViewById(R.id.toolbarDivider)

    val rightActionText: TextView
        get() = rightText

    val titleView: TextView
        get() = titleTv

    init {


        applyAttributes(attrs)
    }

    private fun applyAttributes(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.Toolbar)

            val title = typedArray.getString(R.styleable.Toolbar_titleText)
            setTitle(title)

            val rightIcon = typedArray.getDrawable(R.styleable.Toolbar_iconRight)
            rightIcon?.let { setRightIconDrawable(it) }

            val action = typedArray.getString(R.styleable.Toolbar_textRight)
            action?.let { setTextRight(it) }

            val homeButtonIcon = typedArray.getDrawable(R.styleable.Toolbar_homeButtonIcon)
            homeButtonIcon?.let { setHomeButtonIcon(it) }

            val homeButtonVisible =
                typedArray.getBoolean(R.styleable.Toolbar_homeButtonVisible, true)
            setHomeButtonVisibility(homeButtonVisible)

            val dividerVisible = typedArray.getBoolean(R.styleable.Toolbar_dividerVisible, true)
            toolbarDivider.setVisible(dividerVisible)

            val backgroundAttrDrawable =
                typedArray.getDrawable(R.styleable.Toolbar_contentBackground) ?: ColorDrawable(
                    ContextCompat.getColor(context, R.color.secondary_screen_background)
                )
            toolbarContainer.background = backgroundAttrDrawable

            val textAppearance =
                typedArray.getResourceIdOrNull(R.styleable.Toolbar_titleTextAppearance)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                textAppearance?.let(titleTv::setTextAppearance)
            }

            typedArray.recycle()
        }
    }

    fun setHomeButtonIcon(icon: Drawable) {
        backImg.setImageDrawable(icon)
    }

    fun setTextRight(action: String) {
        rightImg.makeGone()

        rightText.makeVisible()
        rightText.text = action
    }

    fun setRightIconVisible(visible: Boolean) {
        rightImg.setVisible(visible)
    }

    fun setTitle(title: CharSequence?) {
        titleTv.text = title
    }

    fun setTitle(@StringRes titleRes: Int) {
        titleTv.setText(titleRes)
    }

    fun showHomeButton() {
        backImg.makeVisible()
    }

    fun hideHomeButton() {
        backImg.makeGone()
    }

    fun setHomeButtonListener(listener: (View) -> Unit) {
        backImg.setOnClickListener(listener)
    }

    fun setRightIconDrawable(assetIconDrawable: Drawable) {
        rightText.makeGone()

        rightImg.makeVisible()
        rightImg.setImageDrawable(assetIconDrawable)
    }

    fun setRightActionClickListener(listener: (View) -> Unit) {
        rightImg.setOnClickListener(listener)
        rightText.setOnClickListener(listener)
    }

    fun setHomeButtonVisibility(visible: Boolean) {
        backImg.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun addCustomAction(@DrawableRes icon: Int, onClick: OnClickListener): ImageView {
        val actionView = ImageView(context).apply {
            setImageResource(icon)

            layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                val verticalMargin = 16.dp(context)

                val endMarginDp = if (this@Toolbar.toolbarCustomActions.childCount == 0) 16 else 10
                val endMargin = endMarginDp.dp(context)

                val startMargin = 10.dp(context)

                setMargins(startMargin, verticalMargin, endMargin, verticalMargin)
            }

            setOnClickListener(onClick)
        }

        toolbarCustomActions.makeVisible()
        toolbarCustomActions.addView(actionView, 0)

        return actionView
    }
}
