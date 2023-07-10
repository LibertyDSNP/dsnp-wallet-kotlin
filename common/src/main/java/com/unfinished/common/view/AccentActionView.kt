package com.unfinished.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.unfinished.common.R

class AccentActionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val view = View.inflate(context, R.layout.view_accent_action, this)
    private val accentActionIcon: ImageView = view.findViewById(R.id.accentActionIcon)
    private val accentActionText: TextView = view.findViewById(R.id.accentActionText)

    init {


        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL

        setBackgroundResource(R.drawable.bg_primary_list_item)
    }

    fun setText(@StringRes textRes: Int) {
        accentActionText.setText(textRes)
    }

    fun setIcon(@DrawableRes iconRes: Int) {
        accentActionIcon.setImageResource(iconRes)
    }
}