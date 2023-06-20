package com.unfinished.dsnp_wallet_kotlin

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.spec.NavHostEngine
import com.unfinished.dsnp_wallet_kotlin.ui.NavGraphs
import com.unfinished.dsnp_wallet_kotlin.ui.debug.DebugToolbar
import com.unfinished.uikit.MainTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainTheme {
                val engine: NavHostEngine = rememberAnimatedNavHostEngine(
                    rootDefaultAnimations = RootNavGraphDefaultAnimations(
                        enterTransition = { EnterTransition.None },
                        exitTransition = { ExitTransition.None }
                    )
                )
                val navController: NavHostController = engine.rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()

                val debugRoutes = NavGraphs.debug.destinations.map { it.route }

                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (
                        BuildConfig.DEBUG && !debugRoutes.contains(navBackStackEntry?.destination?.route)
                    ) DebugToolbar(
                        navController = navController
                    )

                    DestinationsNavHost(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1F),
                        navGraph = NavGraphs.root,
                        engine = engine,
                        navController = navController
                    )

                }
            }
        }
    }
}