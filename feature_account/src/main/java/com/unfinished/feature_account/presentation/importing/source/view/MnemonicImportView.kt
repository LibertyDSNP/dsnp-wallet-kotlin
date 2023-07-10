package com.unfinished.feature_account.presentation.importing.source.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import com.unfinished.feature_account.R
import com.unfinished.feature_account.databinding.ImportSourceMnemonicBinding
import com.unfinished.common.utils.bindTo
import com.unfinished.common.view.shape.getIdleDrawable
import com.unfinished.feature_account.presentation.importing.source.model.MnemonicImportSource

class MnemonicImportView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ImportSourceView<MnemonicImportSource>(R.layout.import_source_mnemonic, context, attrs, defStyleAttr) {

    var binding: ImportSourceMnemonicBinding

    override val nameInputViews: ImportAccountNameViews
        get() = ImportAccountNameViews(
            nameInput = binding.importMnemonicUsernameInput,
            visibilityDependent = listOf(binding.importMnemnonicUsernameHint)
        )

    init {
        binding = ImportSourceMnemonicBinding.inflate(LayoutInflater.from(context), this)
        binding.importMnemonicContentContainer.background = context.getIdleDrawable()
    }

    override fun observeSource(source: MnemonicImportSource, lifecycleOwner: LifecycleOwner) {
        binding.importMnemonicContent.bindTo(source.mnemonicContentFlow, lifecycleOwner.lifecycle.coroutineScope)
    }
}
