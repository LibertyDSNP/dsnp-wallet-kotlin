package com.unfinished.dsnp_wallet_kotlin.util

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import com.unfinished.dsnp_wallet_kotlin.R

fun Fragment.toast(msg: String, duration: Int = Toast.LENGTH_SHORT){
    Toast.makeText(requireContext(),msg,duration).show()
}

fun View.setBackgroundTint(@ColorInt color: Int){
    ViewCompat.setBackgroundTintList(this, ColorStateList.valueOf(color))
}

fun View.setBackgroundTintColorId(color: Int){
    ViewCompat.setBackgroundTintList(this, ColorStateList.valueOf(ContextCompat.getColor(context,color)))
}

fun View.show(){
    animate().alpha(1f).withEndAction {
        visibility = View.VISIBLE
    }
}

fun View.invisible(){
    animate().alpha(0f).withEndAction { visibility = View.INVISIBLE }
}

fun View.hide(){
    animate().alpha(0f).withEndAction { visibility = View.GONE }
}

fun View.showSnackBar(message: String?){
    message?.let {
        Snackbar.make(this,it,Snackbar.LENGTH_LONG).show()
    }
}

fun ImageView.load(
    url: Any?, onLoadingFinished: () -> Unit = {},
    @DrawableRes placeholder: Int = R.mipmap.ic_launcher,
    cicular: Boolean = false
) {
    val listener = object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            onLoadingFinished()
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            onLoadingFinished()
            return false
        }
    }
    Glide.with(this)
        .load(url)
        .apply { if (cicular) circleCrop() }
        .apply(RequestOptions.placeholderOf(placeholder))
        .listener(listener)
        .into(this)
}

fun View.setOnSafeClickListener(block: (View) -> Unit) {
    setOnClickListener(SafeClickListener { block(it) })
}

fun View.setOnSafeClickListener(timeGap: Long, block: (View) -> Unit) {
    setOnClickListener(SafeClickListener(timeGap) { block(it) })
}


fun String.isValidEmail() =
    isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun View.setVisible(visible: Boolean, falseState: Int = View.GONE) {
    visibility = if (visible) View.VISIBLE else falseState
}

fun TextView.setTextOrHide(newText: String?) {
    if (newText != null) {
        text = newText
        setVisible(true)
    } else {
        setVisible(false)
    }
}

fun View.hideSoftKeyboard() {
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showSoftKeyboard() {
    requestFocus()

    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}
