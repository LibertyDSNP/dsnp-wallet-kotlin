package com.unfinished.dsnp_wallet_kotlin

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.DestinationsNavHost
import com.unfinished.dsnp_wallet_kotlin.ui.NavGraphs
import com.unfinished.dsnp_wallet_kotlin.ui.debug.DebugToolbar
import com.unfinished.uikit.MainTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainTheme {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if(BuildConfig.DEBUG) DebugToolbar()

                    DestinationsNavHost(
                        modifier = Modifier.fillMaxWidth().weight(1F),
                        navGraph = NavGraphs.root
                    )

                }
            }
        }
    }
}