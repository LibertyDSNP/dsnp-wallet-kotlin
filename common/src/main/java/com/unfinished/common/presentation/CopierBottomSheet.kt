package com.unfinished.common.presentation

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import com.unfinished.common.R
import com.unfinished.common.di.FeatureUtils
import com.unfinished.common.resources.ClipboardManager
import com.unfinished.common.view.bottomSheet.list.fixed.FixedListBottomSheet
import com.unfinished.common.view.bottomSheet.list.fixed.item

class CopierBottomSheet(
    context: Context,
    private val value: String,
    private val buttonName: String,
    private val onClipboardMessage: String,
) : FixedListBottomSheet(
    context,
//    viewConfiguration = ViewConfiguration(
//        layout = R.layout.bottom_sheeet_copier,
//        title = { copierValue },
//        container = { copierContainer }
//    )
) {

    constructor(
        context: Context,
        value: String,
        @StringRes buttonNameRes: Int,
        @StringRes onClipboardMessageRes: Int = R.string.common_copied,
    ) : this(
        context,
        value,
        context.getString(buttonNameRes),
        context.getString(onClipboardMessageRes)
    )

    val clipboardManager: ClipboardManager by lazy {
        FeatureUtils.getCommonApi(context).provideClipboardManager()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(value)
        item(R.drawable.ic_copy_outline, buttonName, false) {
            clipboardManager.addToClipboard(value)
            Toast.makeText(context, onClipboardMessage, Toast.LENGTH_LONG)
                .show()
        }
    }
}
