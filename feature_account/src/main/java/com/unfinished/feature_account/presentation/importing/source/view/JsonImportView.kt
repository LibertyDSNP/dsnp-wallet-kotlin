package com.unfinished.feature_account.presentation.importing.source.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import com.unfinished.feature_account.R
import com.unfinished.feature_account.databinding.ImportSourceJsonBinding
import io.novafoundation.nova.common.utils.EventObserver
import io.novafoundation.nova.common.utils.bindTo
import io.novafoundation.nova.common.utils.observe
import io.novafoundation.nova.common.utils.setVisible
import com.unfinished.feature_account.presentation.importing.source.model.JsonImportSource

class JsonImportView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ImportSourceView<JsonImportSource>(R.layout.import_source_json, context, attrs, defStyleAttr) {

    lateinit var binding: ImportSourceJsonBinding

    override val nameInputViews: ImportAccountNameViews
        get() = ImportAccountNameViews(
            nameInput = binding.importJsonUsernameInput,
            visibilityDependent = emptyList()
        )

    init {
        binding = ImportSourceJsonBinding.inflate(LayoutInflater.from(context), this)
    }

    override fun observeSource(source: JsonImportSource, lifecycleOwner: LifecycleOwner) {
        val scope = lifecycleOwner.lifecycle.coroutineScope

        source.jsonContentFlow.observe(scope, binding.importJsonContent::setMessage)

        binding.importJsonContent.setWholeClickListener { source.jsonClicked() }

        source.showJsonInputOptionsEvent.observe(
            lifecycleOwner,
            EventObserver {
                showJsonInputOptionsSheet(source)
            }
        )

        binding.importJsonPasswordInput.content.bindTo(source.passwordFlow, scope)

        binding.importJsonContent.setOnClickListener {
            source.jsonClicked()
        }

        source.showNetworkWarningFlow.observe(scope) {
            binding.importJsonNoNetworkInfo.setVisible(it)
        }
    }

    private fun showJsonInputOptionsSheet(source: JsonImportSource) {
        JsonPasteOptionsSheet(context, source::pasteClicked, source::chooseFileClicked)
            .show()
    }
}