package com.unfinished.feature_account.presentation.export

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.annotation.CallSuper
import androidx.annotation.RequiresApi
import com.unfinished.feature_account.presentation.export.json.confirm.ShareCompletedReceiver
import io.novafoundation.nova.common.base.BaseFragment

abstract class ExportFragment<V : ExportViewModel> : BaseFragment<V>() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    @CallSuper
    override fun subscribe(viewModel: V) {
        viewModel.exportEvent.observeEvent(::shareTextWithCallback)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    private fun shareTextWithCallback(text: String) {
        val title = getString(io.novafoundation.nova.common.R.string.common_share)

        val intent = Intent(Intent.ACTION_SEND)
            .putExtra(Intent.EXTRA_TEXT, text)
            .setType("text/plain")

        val receiver = Intent(requireContext(), ShareCompletedReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, receiver, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val chooser = Intent.createChooser(intent, title, pendingIntent.intentSender)

        startActivity(chooser)
    }
}
