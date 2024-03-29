package com.unfinished.account.presentation.importing.source.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.lifecycle.LifecycleOwner
import com.unfinished.common.view.InputField
import com.unfinished.account.presentation.importing.ImportAccountViewModel
import com.unfinished.account.presentation.importing.source.model.ImportSource
import com.unfinished.account.presentation.mixin.setupAccountNameChooserUi

class ImportAccountNameViews(
    val nameInput: InputField,
    val visibilityDependent: List<View>,
)

abstract class ImportSourceView<S : ImportSource> @JvmOverloads constructor(
    @LayoutRes layoutId: Int,
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {

    protected abstract val nameInputViews: ImportAccountNameViews

    init {
        View.inflate(context, layoutId, this)

        orientation = VERTICAL
    }

    abstract fun observeSource(source: S, lifecycleOwner: LifecycleOwner)

    fun observeCommon(viewModel: ImportAccountViewModel, lifecycleOwner: LifecycleOwner) {
        setupAccountNameChooserUi(viewModel, nameInputViews.nameInput, lifecycleOwner, nameInputViews.visibilityDependent)
    }
}
