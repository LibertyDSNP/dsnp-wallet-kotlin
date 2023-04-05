package io.novafoundation.nova.common.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import io.novafoundation.nova.common.di.CommonApi

abstract class BaseActivity : AppCompatActivity() {

    override fun attachBaseContext(base: Context) {
        val commonApi = (base.applicationContext as CommonApi)
        val contextManager = commonApi.contextManager()
        applyOverrideConfiguration(contextManager.setLocale(base).resources.configuration)
        super.attachBaseContext(contextManager.setLocale(base))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val decorView = window.decorView
        decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            )
        setContentView(layoutResource())
        initViews()
        subscribe()
    }

    abstract fun layoutResource(): View

    abstract fun initViews()

    abstract fun subscribe()

    abstract fun changeLanguage()
}
