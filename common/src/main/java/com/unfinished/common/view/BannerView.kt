package com.unfinished.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import com.unfinished.common.R
import com.unfinished.common.utils.dp
import com.unfinished.common.utils.dpF

class BannerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : MaterialCardView(context, attrs, defStyle) {

    private val view: View = View.inflate(context, R.layout.view_banner, this)
    private val bannerBackground: ConstraintLayout = view.findViewById(R.id.bannerBackground)
    private val bannerContent: FrameLayout = view.findViewById(R.id.bannerContent)
    private val bannerImage: ImageView = view.findViewById(R.id.bannerImage)

    init {

        cardElevation = 0f
        radius = 12f.dpF(context)
        strokeWidth = 1.dp(context)

        strokeColor = ContextCompat.getColor(context, R.color.container_border)

        applyAttributes(attrs)
    }

    private fun applyAttributes(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BannerView)

            val image = typedArray.getDrawable(R.styleable.BannerView_android_src)
            bannerImage.setImageDrawable(image)

            val background = typedArray.getDrawable(R.styleable.BannerView_bannerBackground)
            bannerBackground.background = background

            typedArray.recycle()
        }
    }

    override fun addView(child: View, params: ViewGroup.LayoutParams?) {
        if (child.id == R.id.bannerBackground) {
            super.addView(child, params)
        } else {
            bannerContent.addView(child, params)
        }
    }
}
