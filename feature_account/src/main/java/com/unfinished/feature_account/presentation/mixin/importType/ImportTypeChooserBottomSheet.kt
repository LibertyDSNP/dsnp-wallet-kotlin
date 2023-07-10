package com.unfinished.feature_account.presentation.mixin.importType

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.unfinished.feature_account.databinding.ItemSourceTypeBinding
import com.unfinished.common.R
import com.unfinished.feature_account.presentation.model.account.add.SecretType
import com.unfinished.common.view.bottomSheet.list.fixed.FixedListBottomSheet

class ImportTypeChooserBottomSheet(
    context: Context,
    private val onChosen: (SecretType) -> Unit,
    private val allowedSources: Set<SecretType>
) : FixedListBottomSheet(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitle(R.string.account_select_secret_source)

        item(
            type = SecretType.MNEMONIC,
            title = R.string.recovery_passphrase,
            subtitle = R.string.account_mnmonic_length_variants,
            icon = R.drawable.ic_mnemonic_phrase
        )

        item(
            type = SecretType.SEED,
            title = R.string.recovery_raw_seed,
            subtitle = R.string.account_private_key,
            icon = R.drawable.ic_raw_seed
        )

        item(
            type = SecretType.JSON,
            title = R.string.recovery_json,
            subtitle = R.string.account_json_file,
            icon = R.drawable.ic_file_outline
        )
    }

    private fun item(
        type: SecretType,
        @StringRes title: Int,
        @StringRes subtitle: Int,
        @DrawableRes icon: Int
    ) {
        if (type !in allowedSources) return

        item(com.unfinished.feature_account.R.layout.item_source_type) {
            it.findViewById<ImageView>(com.unfinished.feature_account.R.id.itemSourceTypeIcon).setImageResource(icon)
            it.findViewById<TextView>(com.unfinished.feature_account.R.id.itemSourceTypeTitle).setText(title)
            it.findViewById<TextView>(com.unfinished.feature_account.R.id.itemSourceTypeSubtitle).setText(subtitle)
            it.setDismissingClickListener { onChosen(type) }
        }
    }
}
