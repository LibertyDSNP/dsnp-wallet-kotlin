package com.unfinished.common.view.bottomSheet

import android.content.Context
import android.content.DialogInterface
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.unfinished.common.R
import com.unfinished.common.utils.DialogExtensions

abstract class BaseBottomSheet(context: Context) : BottomSheetDialog(context, R.style.BottomSheetDialog), DialogExtensions {

    final override val dialogInterface: DialogInterface
        get() = this
}
