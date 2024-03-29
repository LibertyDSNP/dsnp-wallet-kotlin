package com.unfinished.account.presentation.importing.source.view

import android.content.Context
import android.os.Bundle
import com.unfinished.common.R
import com.unfinished.common.view.bottomSheet.list.fixed.FixedListBottomSheet
import com.unfinished.common.view.bottomSheet.list.fixed.item

class JsonPasteOptionsSheet(
    context: Context,
    val onPaste: () -> Unit,
    val onOpenFile: () -> Unit
) : FixedListBottomSheet(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitle(R.string.recovery_json)

        item(icon = R.drawable.ic_copy_outline, titleRes = R.string.import_json_paste) {
            onPaste()
        }

        item(icon = R.drawable.ic_json_file_upload_outline, titleRes = R.string.recover_json_upload) {
            onOpenFile()
        }
    }
}
