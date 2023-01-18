package com.unfinished.dsnp_wallet_kotlin

import android.app.Application
import com.unfinished.dsnp_wallet_kotlin.util.ReleaseTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree());
        } else {
            Timber.plant(ReleaseTree())
        }
    }
}