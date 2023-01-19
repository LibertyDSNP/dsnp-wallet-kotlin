package com.unfinished.dsnp_wallet_kotlin.util

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.FontRes

private fun clickableSpan(
    typeface: Typeface? = null,
    @ColorRes highlightTextColor: Int? = null,
    onClick: () -> Unit
) = object : ClickableSpan() {
    override fun updateDrawState(ds: TextPaint) {
        ds.typeface = typeface
        ds.isUnderlineText = false
        highlightTextColor?.let { ds.color = it }
    }

    override fun onClick(widget: View) {
        onClick()
    }
}

class SpannableBuilder(
    val content: String,
    val typeface: Typeface? = null,
    @ColorRes val highlightTextColor: Int? = null
) {

    private val buildingSpannable = SpannableString(content)

    fun clickable(text: String, onClick: () -> Unit) {
        val startIndex = content.indexOf(text)

        if (startIndex == -1) {
            return
        }

        val endIndex = startIndex + text.length

        buildingSpannable.setSpan(clickableSpan(typeface,highlightTextColor,onClick), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    fun build() = buildingSpannable
}

fun createSpannable(
    content: String,
    typeface: Typeface? = null,
    @ColorRes highlightTextColor: Int? = null,
    block: SpannableBuilder.() -> Unit
): Spannable {
    val builder = SpannableBuilder(content, typeface, highlightTextColor)

    builder.block()

    return builder.build()
}


