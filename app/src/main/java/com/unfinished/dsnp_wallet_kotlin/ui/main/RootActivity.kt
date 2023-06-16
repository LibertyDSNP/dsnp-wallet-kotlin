package com.unfinished.dsnp_wallet_kotlin.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.unfinished.dsnp_wallet_kotlin.R
import com.unfinished.dsnp_wallet_kotlin.databinding.ActivityRootBinding
import com.unfinished.dsnp_wallet_kotlin.root.NavigationHolder
import com.unfinished.dsnp_wallet_kotlin.ui.onboarding.LandingViewModel
import com.unfinished.dsnp_wallet_kotlin.util.hide
import dagger.hilt.android.AndroidEntryPoint
import io.novafoundation.nova.common.base.BaseActivity
import io.novafoundation.nova.common.resources.ContextManager
import io.novafoundation.nova.common.utils.systemCall.SystemCallExecutor
import javax.inject.Inject


@AndroidEntryPoint
class RootActivity : BaseActivity() {

    val TAG: String = "MainActivity"
    private lateinit var binding: ActivityRootBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    val viewModel by viewModels<RootViewModel>()

    @Inject
    lateinit var navigationHolder: NavigationHolder
    @Inject
    lateinit var systemCallExecutor: SystemCallExecutor
    @Inject
    lateinit var contextManager: ContextManager

    private val navController: NavController by lazy {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navHostFragment.navController
    }

    override fun layoutResource(): View {
        binding = ActivityRootBinding.inflate(layoutInflater)
         return binding.root
    }

    override fun initViews() {
        setSupportActionBar(binding.toolbar)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun subscribe() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contextManager.attachActivity(this)
        navigationHolder.attach(navController)
        intent?.let(::processIntent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        processIntent(intent)
    }

    private fun processIntent(intent: Intent) {
        val uri = intent.data?.toString()

        uri?.let { viewModel.externalUrlOpened(uri) }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        viewModel.restoredAfterConfigChange()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!systemCallExecutor.onActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun changeLanguage() {
        viewModel.noticeLanguageLanguage()
        recreate()
    }

    override fun onStop() {
        super.onStop()

        viewModel.noticeInBackground()
    }

    override fun onStart() {
        super.onStart()

        viewModel.noticeInForeground()
    }

    override fun onDestroy() {
        super.onDestroy()
        contextManager.detachActivity()
        navigationHolder.detach()
    }

    companion object {
        const val REDIRECT_URL_BASE = "nova://buy-success"
    }
}