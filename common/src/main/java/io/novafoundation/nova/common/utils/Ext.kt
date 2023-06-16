package io.novafoundation.nova.common.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.ColorInt
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import io.novafoundation.nova.common.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import java.net.URLEncoder

fun Activity.showToast(msg: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, msg, duration).show()
}

fun <T> MutableLiveData<T>.setValueIfNew(newValue: T) {
    if (this.value != newValue) value = newValue
}

fun View.makeVisible() {
    this.visibility = View.VISIBLE
}

fun View.makeInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.makeGone() {
    this.visibility = View.GONE
}

fun Fragment.hideKeyboard() {
    requireActivity().currentFocus?.hideSoftKeyboard()
}

fun Fragment.showBrowser(link: String) = requireContext().showBrowser(link)

fun Context.showBrowser(link: String) {
    val intent = Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(link) }

    try {
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(this, R.string.common_cannot_open_link, Toast.LENGTH_SHORT)
            .show()
    }
}

fun Context.sendEmailIntent(
    targetEmail: String,
    title: String = getString(R.string.common_email_chooser_title)
) {
    val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
        putExtra(Intent.EXTRA_EMAIL, targetEmail)
        type = "message/rfc822"
        data = Uri.parse("mailto:$targetEmail")
    }
    startActivity(Intent.createChooser(emailIntent, title))
}

fun @receiver:ColorInt Int.toHexColor(): String {
    val withoutAlpha = 0xFFFFFF and this

    return "#%06X".format(withoutAlpha)
}

fun String.urlEncoded() = URLEncoder.encode(this, Charsets.UTF_8.displayName())

fun CoroutineScope.childScope(supervised: Boolean = true): CoroutineScope {
    val parentJob = coroutineContext[Job]

    val job = if (supervised) SupervisorJob(parent = parentJob) else Job(parent = parentJob)

    return CoroutineScope(coroutineContext + job)
}

fun Int.asBoolean() = this != 0

inline fun <reified T : ViewModel> FragmentActivity.assistedViewModel(
    crossinline viewModelProducer: (SavedStateHandle) -> T
) = viewModels<T> {
    object : AbstractSavedStateViewModelFactory(this@assistedViewModel, intent.extras) {
        override fun <T : ViewModel> create(key: String, modelClass: Class<T>, handle: SavedStateHandle) =
            viewModelProducer(handle) as T
    }
}

inline fun <reified T : ViewModel> Fragment.assistedViewModel(
    crossinline viewModelProducer: (SavedStateHandle) -> T
) = viewModels<T> {
    object : AbstractSavedStateViewModelFactory(this@assistedViewModel, arguments) {
        override fun <T : ViewModel> create(key: String, modelClass: Class<T>, handle: SavedStateHandle) =
            viewModelProducer(handle) as T
    }
}

/*
inline fun <reified T : ViewModel> Fragment.assistedNavGraphViewModel(
    @IdRes navGraphId: Int,
    crossinline viewModelProducer: (SavedStateHandle) -> T
) = navGraphViewModels<T>(navGraphId) {
    object : AbstractSavedStateViewModelFactory(this, arguments) {
        override fun <T : ViewModel> create(key: String, modelClass: Class<T>, handle: SavedStateHandle) =
            viewModelProducer(handle) as T
    }
}
*/

inline fun <reified T : ViewModel> Fragment.assistedActivityViewModel(
    crossinline viewModelProducer: (SavedStateHandle) -> T
) = activityViewModels<T> {
    object : AbstractSavedStateViewModelFactory(this@assistedActivityViewModel, arguments) {
        override fun <T : ViewModel> create(key: String, modelClass: Class<T>, handle: SavedStateHandle) =
            viewModelProducer(handle) as T
    }
}