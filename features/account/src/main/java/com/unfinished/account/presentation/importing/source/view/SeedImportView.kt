package com.unfinished.account.presentation.importing.source.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import com.unfinished.account.R
import com.unfinished.account.databinding.ImportSourceSeedBinding
import com.unfinished.common.utils.bindTo
import com.unfinished.common.view.shape.getIdleDrawable
import com.unfinished.account.presentation.importing.source.model.RawSeedImportSource

class SeedImportView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ImportSourceView<RawSeedImportSource>(R.layout.import_source_seed, context, attrs, defStyleAttr) {

    var binding: ImportSourceSeedBinding

    override val nameInputViews: ImportAccountNameViews
        get() = ImportAccountNameViews(
            nameInput = binding.importSeedUsernameInput,
            visibilityDependent = listOf(binding.importSeedUsernameHint)
        )

    init {
        binding = ImportSourceSeedBinding.inflate(LayoutInflater.from(context), this)
        binding.importSeedContentContainer.background = context.getIdleDrawable()
    }

    override fun observeSource(source: RawSeedImportSource, lifecycleOwner: LifecycleOwner) {
        binding.importSeedContent.bindTo(source.rawSeedFlow, lifecycleOwner.lifecycle.coroutineScope)
    }
}
