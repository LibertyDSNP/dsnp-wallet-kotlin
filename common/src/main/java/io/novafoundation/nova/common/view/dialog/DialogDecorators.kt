package io.novafoundation.nova.common.view.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import io.novafoundation.nova.common.R
import io.novafoundation.nova.common.utils.makeInvisible
import io.novafoundation.nova.common.utils.setOnSafeClickListener
import io.novafoundation.nova.common.utils.themed
import io.novafoundation.nova.common.view.PrimaryButton


typealias DialogClickHandler = () -> Unit

typealias DialogDecorator = AlertDialog.Builder.() -> Unit

inline fun dialog(
    context: Context,
    title: String,
    subtitle: String,
    description: String,
    buttonText: String,
    secondaryText: String? = null,
    cancelable: Boolean,
    crossinline primaryAction: () -> Unit,
    crossinline secondaryAction: () -> Unit,
    crossinline closeAction: () -> Unit,
) : Dialog {
    val metrics = context.resources.displayMetrics
    val dialog = Dialog(context, android.R.style.Theme_Dialog)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(R.layout.dialog_info)
    dialog.window!!.setGravity(Gravity.CENTER)
    dialog.window!!.setLayout(6 * metrics.widthPixels / 7, WindowManager.LayoutParams.WRAP_CONTENT)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCancelable(cancelable)
    dialog.apply {
        findViewById<TextView>(R.id.dialog_info_title).text = title
        findViewById<TextView>(R.id.dialog_info_subtitle).text = subtitle
        findViewById<TextView>(R.id.dialog_info_description).text = description
        findViewById<TextView>(R.id.dialog_info_secondary_text).apply {
            paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
            setOnSafeClickListener { secondaryAction.invoke() }
            secondaryText?.let { text = it } ?: kotlin.run { makeInvisible() }
        }
        findViewById<PrimaryButton>(R.id.dialog_info_primary_action).apply {
            text = buttonText
            setOnSafeClickListener { primaryAction.invoke() }
        }
        findViewById<ImageView>(R.id.dialog_info_close).setOnSafeClickListener {
            dismiss()
            closeAction.invoke()
        }
    }
    return dialog
}

inline fun dialog(
    context: Context,
    decorator: DialogDecorator
) {
    val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.WhiteOverlay))
        .setCancelable(false)

    builder.decorator()

    builder.show()
}

fun infoDialog(
    context: Context,
    decorator: DialogDecorator
) {
    dialog(context) {
        setPositiveButton(R.string.common_ok, null)

        decorator()
    }
}

fun warningDialog(
    context: Context,
    onConfirm: DialogClickHandler,
    @StringRes confirmTextRes: Int = R.string.common_continue,
    @StringRes cancelTextRes: Int = R.string.common_cancel,
    onCancel: DialogClickHandler? = null,
    decorator: DialogDecorator? = null
) {
    dialog(context.themed(R.style.AccentAlertDialogTheme_Reversed)) {
        setPositiveButton(confirmTextRes) { _, _ -> onConfirm() }
        setNegativeButton(cancelTextRes) { _, _ -> onCancel?.invoke() }

        decorator?.invoke(this)
    }
}

fun errorDialog(
    context: Context,
    onConfirm: DialogClickHandler? = null,
    @StringRes confirmTextRes: Int = R.string.common_ok,
    decorator: DialogDecorator? = null
) {
    dialog(context) {
        setTitle(R.string.common_error_general_title)
        setPositiveButton(confirmTextRes) { _, _ -> onConfirm?.invoke() }

        decorator?.invoke(this)
    }
}

fun retryDialog(
    context: Context,
    onRetry: DialogClickHandler? = null,
    onCancel: DialogClickHandler? = null,
    decorator: DialogDecorator? = null
) {
    dialog(context) {
        setTitle(R.string.common_error_general_title)
        setPositiveButton(R.string.common_retry) { _, _ -> onRetry?.invoke() }
        setNegativeButton(R.string.common_ok) { _, _ -> onCancel?.invoke() }

        decorator?.invoke(this)
    }
}
