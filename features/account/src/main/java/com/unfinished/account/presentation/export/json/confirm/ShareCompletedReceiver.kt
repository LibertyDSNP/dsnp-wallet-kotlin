package com.unfinished.account.presentation.export.json.confirm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.unfinished.account.presentation.AccountRouter
import javax.inject.Inject


class ShareCompletedReceiver : BroadcastReceiver() {

    @Inject
    lateinit var router: AccountRouter

    override fun onReceive(context: Context, intent: Intent) {
        router.finishExportFlow()
    }
}
