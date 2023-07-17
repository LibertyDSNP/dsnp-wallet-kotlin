package com.unfinished.common.view.shape

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.StateListDrawable
import android.util.StateSet
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.unfinished.common.R

fun Int.toColorStateList() = ColorStateList.valueOf(this)

fun Context.getRippleMask(cornerSizeDp: Int = 12): Drawable {
    return getRoundedCornerDrawableFromColors(Color.WHITE, null, cornerSizeDp)
}

fun Context.addRipple(
    drawable: Drawable? = null,
    mask: Drawable? = getRippleMask(),
    @ColorInt rippleColor: Int = ContextCompat.getColor(this, R.color.cell_background_pressed)
): Drawable {
    return RippleDrawable(rippleColor.toColorStateList(), drawable, mask)
}

fun Context.getCornersStateDrawable(
    disabledDrawable: Drawable = getDisabledDrawable(),
    focusedDrawable: Drawable = getFocusedDrawable(),
    idleDrawable: Drawable = getIdleDrawable(),
): Drawable {
    return StateListDrawable().apply {
        addState(intArrayOf(-android.R.attr.state_enabled), disabledDrawable)
        addState(intArrayOf(android.R.attr.state_focused), focusedDrawable)
        addState(StateSet.WILD_CARD, idleDrawable)
    }
}

fun Context.getCornersCheckableDrawable(
    checked: Drawable,
    unchecked: Drawable
): Drawable {
    return StateListDrawable().apply {
        addState(intArrayOf(android.R.attr.state_checked), checked)
        addState(StateSet.WILD_CARD, unchecked)
    }
}

fun Context.getInputBackground() = getCornersStateDrawable(
    focusedDrawable = getRoundedCornerDrawableFromColors(
        fillColor = ContextCompat.getColor(this, R.color.input_background),
        strokeColor = ContextCompat.getColor(this, R.color.active_border),
        strokeSizeInDp = 1f
    ),
    idleDrawable = getRoundedCornerDrawable(R.color.input_background)
)

fun Context.getFocusedDrawable(): Drawable = getRoundedCornerDrawable(strokeColorRes = R.color.active_border)
fun Context.getDisabledDrawable(): Drawable = getRoundedCornerDrawable(fillColorRes = R.color.input_background)
fun Context.getIdleDrawable(): Drawable = getRoundedCornerDrawable(strokeColorRes = R.color.container_border)
fun Context.getBlockDrawable(@ColorRes strokeColorRes: Int? = null): Drawable {
    return getRoundedCornerDrawable(fillColorRes = R.color.block_background, strokeColorRes = strokeColorRes)
}

fun Context.getRoundedCornerDrawable(
    @ColorRes fillColorRes: Int = R.color.secondary_screen_background,
    @ColorRes strokeColorRes: Int? = null,
    cornerSizeInDp: Int = 12,
    strokeSizeInDp: Float = 1.0f,
): Drawable {
    val fillColor = ContextCompat.getColor(this, fillColorRes)
    val strokeColor = strokeColorRes?.let { ContextCompat.getColor(this, it) }

    return getRoundedCornerDrawableFromColors(fillColor, strokeColor, cornerSizeInDp, strokeSizeInDp)
}

fun Context.getTopRoundedCornerDrawable(
    @ColorRes fillColorRes: Int = R.color.secondary_screen_background,
    @ColorRes strokeColorRes: Int? = null,
    cornerSizeInDp: Int = 12,
    strokeSizeInDp: Float = 1.0f,
): Drawable {
    val fillColor = ContextCompat.getColor(this, fillColorRes)
    val strokeColor = strokeColorRes?.let { ContextCompat.getColor(this, it) }

    return getTopRoundedCornerDrawableFromColors(fillColor, strokeColor, cornerSizeInDp, strokeSizeInDp)
}

fun Context.getTopRoundedCornerDrawableFromColors(
    @ColorInt fillColor: Int = ContextCompat.getColor(this, R.color.secondary_screen_background),
    @ColorInt strokeColor: Int? = null,
    cornerSizeInDp: Int = 12,
    strokeSizeInDp: Float = 1.0f,
): Drawable {
    return cornerDrawableFromColors(
        fillColor = fillColor,
        strokeColor = strokeColor,
        cornerSizeInDp = cornerSizeInDp,
        strokeSizeInDp = strokeSizeInDp,
        shapeBuilder = { cornerSizePx ->
            ShapeAppearanceModel.Builder()
                .setTopLeftCorner(CornerFamily.ROUNDED, cornerSizePx)
                .setTopRightCorner(CornerFamily.ROUNDED, cornerSizePx)
                .build()
        }
    )
}

fun Context.getRoundedCornerDrawableFromColors(
    @ColorInt fillColor: Int = ContextCompat.getColor(this, R.color.secondary_screen_background),
    @ColorInt strokeColor: Int? = null,
    cornerSizeInDp: Int = 12,
    strokeSizeInDp: Float = 1.0f,
): Drawable {
    return cornerDrawableFromColors(
        fillColor = fillColor,
        strokeColor = strokeColor,
        cornerSizeInDp = cornerSizeInDp,
        strokeSizeInDp = strokeSizeInDp,
        shapeBuilder = { cornerSizePx ->
            ShapeAppearanceModel.Builder()
                .setAllCorners(CornerFamily.ROUNDED, cornerSizePx)
                .build()
        }
    )
}

private fun Context.cornerDrawableFromColors(
    @ColorInt fillColor: Int,
    @ColorInt strokeColor: Int?,
    cornerSizeInDp: Int,
    strokeSizeInDp: Float,
    shapeBuilder: (cornerSize: Float) -> ShapeAppearanceModel
): Drawable {
    val density = resources.displayMetrics.density

    val cornerSizePx = density * cornerSizeInDp
    val strokeSizePx = density * strokeSizeInDp

    return MaterialShapeDrawable(shapeBuilder(cornerSizePx)).apply {
        setFillColor(ColorStateList.valueOf(fillColor))

        strokeColor?.let {
            setStroke(strokeSizePx, it)
        }
    }
}
