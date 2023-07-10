package com.unfinished.common.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.unfinished.common.R
import com.unfinished.common.utils.WithContextExtensions
import com.unfinished.common.utils.makeGone
import com.unfinished.common.utils.makeVisible

class AccountInfoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), WithContextExtensions by WithContextExtensions(context) {

    private val view = View.inflate(context, R.layout.view_account_info, this)
    private val accountAction: ImageView = view.findViewById(R.id.accountAction)
    private val accountAddressText: TextView = view.findViewById(R.id.accountAddressText)
    private val accountIcon: ImageView = view.findViewById(R.id.accountIcon)
    private val accountTitle: TextView = view.findViewById(R.id.accountTitle)

    init {


        background = getRoundedCornerDrawable(fillColorRes = R.color.block_background).withRippleMask()

        isFocusable = true
        isClickable = true

        applyAttributes(attrs)
    }

    private fun applyAttributes(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AccountInfoView)

            val actionIcon = typedArray.getDrawable(R.styleable.AccountInfoView_accountActionIcon)
            actionIcon?.let(::setActionIcon)

            val textVisible = typedArray.getBoolean(R.styleable.AccountInfoView_textVisible, true)
            accountAddressText.visibility = if (textVisible) View.VISIBLE else View.GONE

            typedArray.recycle()
        }
    }

    fun setActionIcon(icon: Drawable) {
        accountAction.setImageDrawable(icon)
    }

    fun setActionListener(clickListener: (View) -> Unit) {
        accountAction.setOnClickListener(clickListener)
    }

    fun setWholeClickListener(listener: (View) -> Unit) {
        setOnClickListener(listener)

        setActionListener(listener)
    }

    fun setTitle(accountName: String) {
        accountTitle.text = accountName
    }

    fun setText(address: String) {
        accountAddressText.text = address
    }

    fun setAccountIcon(icon: Drawable) {
        accountIcon.setImageDrawable(icon)
    }

    fun hideBody() {
        accountAddressText.makeGone()
    }

    fun showBody() {
        accountAddressText.makeVisible()
    }
}
