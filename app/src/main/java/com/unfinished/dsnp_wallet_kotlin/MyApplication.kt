package com.unfinished.dsnp_wallet_kotlin

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import coil.ImageLoader
import com.unfinished.dsnp_wallet_kotlin.util.ReleaseTree
import dagger.hilt.android.HiltAndroidApp
import com.unfinished.common.data.network.rpc.SocketSingleRequestExecutor
import com.unfinished.common.di.CommonApi
import com.unfinished.common.resources.*
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class MyApplication: Application(), CommonApi {

    @Inject lateinit var imageLoader: ImageLoader
    @Inject lateinit var clipboardManager: ClipboardManager
    @Inject lateinit var contextManager: ContextManager
    @Inject lateinit var singleRequestExecutor: SocketSingleRequestExecutor
    private val languagesHolder: LanguagesHolder = LanguagesHolder()

    override fun attachBaseContext(base: Context) {
        val contextManager = ContextManager.getInstanceOrInit(base, languagesHolder)
        super.attachBaseContext(contextManager.setLocale(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val contextManager = ContextManager.getInstanceOrInit(this, languagesHolder)
        contextManager.setLocale(this)
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree());
        } else {
            Timber.plant(ReleaseTree())
        }
    }

    override fun imageLoader() = imageLoader
    override fun provideClipboardManager() = clipboardManager
    override fun contextManager() = contextManager
    override fun socketSingleRequestExecutor() = singleRequestExecutor
}