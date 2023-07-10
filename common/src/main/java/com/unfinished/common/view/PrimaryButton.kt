package com.unfinished.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.View
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.isProgressActive
import com.github.razir.progressbutton.showProgress
import com.google.android.material.button.MaterialButton
import com.unfinished.common.R
import com.unfinished.common.presentation.DescriptiveButtonState
import com.unfinished.common.utils.dp
import com.unfinished.common.utils.getColorFromAttr
import com.unfinished.common.utils.getEnum
import com.unfinished.common.utils.setVisible
import com.unfinished.common.utils.useAttributes
import com.unfinished.common.view.shape.addRipple
import com.unfinished.common.view.shape.getCornersStateDrawable
import com.unfinished.common.view.shape.getRoundedCornerDrawableFromColors

enum class ButtonState {
    NORMAL,
    DISABLED,
    PROGRESS,
    GONE
}

class PrimaryButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : AppCompatTextView(ContextThemeWrapper(context, R.style.Widget_Nova_Button), attrs, defStyle) {

    enum class Appearance {

        PRIMARY {
            override fun disabledColor(context: Context) = ContextCompat.getColor(context,R.color.button_background_inactive)

            override fun enabledColor(context: Context) = ContextCompat.getColor(context,R.color.button_background_primary)
        },
        PRIMARY_TRANSPARENT {
            override fun disabledColor(context: Context) = ContextCompat.getColor(context,R.color.button_background_inactive_on_gradient)

            override fun enabledColor(context: Context) = ContextCompat.getColor(context,R.color.button_background_primary)
        },
        SECONDARY {
            override fun disabledColor(context: Context) = ContextCompat.getColor(context,R.color.button_background_inactive)

            override fun enabledColor(context: Context) = ContextCompat.getColor(context,R.color.button_background_secondary)
        },
        SECONDARY_TRANSPARENT {
            override fun disabledColor(context: Context) = ContextCompat.getColor(context,R.color.button_background_inactive_on_gradient)

            override fun enabledColor(context: Context) = ContextCompat.getColor(context,R.color.button_background_secondary)
        },

        PRIMARY_POSITIVE {
            override fun disabledColor(context: Context) = ContextCompat.getColor(context,R.color.button_background_inactive)

            override fun enabledColor(context: Context) = ContextCompat.getColor(context,R.color.button_background_approve)
        },

        PRIMARY_NEGATIVE {
            override fun disabledColor(context: Context) = ContextCompat.getColor(context,R.color.button_background_inactive)

            override fun enabledColor(context: Context) = ContextCompat.getColor(context,R.color.button_background_reject)
        };

        @ColorInt
        abstract fun disabledColor(context: Context): Int

        @ColorInt
        abstract fun enabledColor(context: Context): Int
    }

    enum class Size(val heightDp: Int, val cornerSizeDp: Int) {
        LARGE(48, 30),
        SMALL(44, 30);
    }

    private var cachedText: String? = null

    private lateinit var size: Size

    private var preparedForProgress = false

    init {
        attrs?.let(this::applyAttrs)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        var newHeightMeasureSpec = heightMeasureSpec
        if (heightMode == MeasureSpec.AT_MOST) {
            newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(size.heightDp.dp(context), heightMode)
        }

        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec)
    }

    private fun applyAttrs(attrs: AttributeSet) = context.useAttributes(attrs, R.styleable.PrimaryButton) { typedArray ->
        val appearance = typedArray.getEnum(R.styleable.PrimaryButton_appearance, Appearance.PRIMARY)
        size = typedArray.getEnum(R.styleable.PrimaryButton_size, Size.LARGE)

        setAppearance(appearance, cornerSizeDp = size.cornerSizeDp)
    }

    fun prepareForProgress(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.bindProgressButton(this)

        preparedForProgress = true
    }

    fun setState(state: ButtonState) {
        isEnabled = state == ButtonState.NORMAL
        setVisible(state != ButtonState.GONE)

        if (state == ButtonState.PROGRESS) {
            checkPreparedForProgress()

            showProgress()
        } else {
            hideProgress()
        }
    }

    private fun setAppearance(appearance: Appearance, cornerSizeDp: Int) = with(context) {
        val activeState = getRoundedCornerDrawableFromColors(appearance.enabledColor(this), cornerSizeInDp = cornerSizeDp)
        val baseBackground = getCornersStateDrawable(
            disabledDrawable = getRoundedCornerDrawableFromColors(appearance.disabledColor(this), cornerSizeInDp = cornerSizeDp),
            focusedDrawable = activeState,
            idleDrawable = activeState
        )

        val rippleColor = getColorFromAttr(androidx.appcompat.R.attr.colorControlHighlight)
        val background = addRipple(baseBackground, mask = null, rippleColor = rippleColor)

        setBackground(background)
    }

    private fun checkPreparedForProgress() {
        if (!preparedForProgress) {
            throw IllegalArgumentException("You must call prepareForProgress() first!")
        }
    }

    private fun hideProgress() {
        if (isProgressActive()) {
            hideProgress(cachedText)
        }
    }

    private fun showProgress() {
        if (isProgressActive()) return

        cachedText = text.toString()

        showProgress {
            progressColor = currentTextColor
        }
    }
}

fun PrimaryButton.setProgress(show: Boolean) {
    setState(if (show) ButtonState.PROGRESS else ButtonState.NORMAL)
}

fun PrimaryButton.setState(descriptiveButtonState: DescriptiveButtonState) {
    when (descriptiveButtonState) {
        is DescriptiveButtonState.Disabled -> {
            setState(ButtonState.DISABLED)
            text = descriptiveButtonState.reason
        }
        is DescriptiveButtonState.Enabled -> {
            setState(ButtonState.NORMAL)
            text = descriptiveButtonState.action
        }
        DescriptiveButtonState.Loading -> {
            setState(ButtonState.PROGRESS)
        }
        DescriptiveButtonState.Gone -> {
            setState(ButtonState.GONE)
        }
    }
}

fun MaterialButton.setState(descriptiveButtonState: DescriptiveButtonState){
    when (descriptiveButtonState) {
        is DescriptiveButtonState.Disabled -> {
           isEnabled = false
        }
        is DescriptiveButtonState.Enabled -> {
           isEnabled = true
        }
        DescriptiveButtonState.Loading -> {
         visibility = View.INVISIBLE
        }
        DescriptiveButtonState.Gone -> {
          visibility = View.GONE
        }
    }
}
