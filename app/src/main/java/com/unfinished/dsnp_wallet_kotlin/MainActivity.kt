package com.unfinished.dsnp_wallet_kotlin

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.spec.NavHostEngine
import com.unfinished.dsnp_wallet_kotlin.deeplink.DeeplinkViewModel
import com.unfinished.dsnp_wallet_kotlin.ui.NavGraphs
import com.unfinished.dsnp_wallet_kotlin.ui.common.bottomsheet.viewmodel.BottomSheetViewModel
import com.unfinished.dsnp_wallet_kotlin.ui.debug.DebugToolbar
import com.unfinished.dsnp_wallet_kotlin.ui.destinations.LandingPageScreenDestination
import com.unfinished.dsnp_wallet_kotlin.ui.common.dialog.compose.CloseableDialog
import com.unfinished.dsnp_wallet_kotlin.ui.common.dialog.viewmodel.DialogViewModel
import com.unfinished.dsnp_wallet_kotlin.ui.home.viewmmodel.IdentityViewModel
import com.unfinished.dsnp_wallet_kotlin.ui.onboarding.viewmodel.CreateIdentityViewModel
import com.unfinished.dsnp_wallet_kotlin.util.exts.safeGetBackStackEntry
import com.unfinished.uikit.MainTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val identityViewModel: IdentityViewModel by viewModels()
    private val dialogViewModel: DialogViewModel by viewModels()
    private val bottomSheetViewModel: BottomSheetViewModel by viewModels()

    private val deeplinkViewModel: DeeplinkViewModel by viewModels()

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
                var showDebug by remember {
                    mutableStateOf(true)
                }

                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (
                        BuildConfig.FLAVOR == "dev"
                        && !debugRoutes.contains(navBackStackEntry?.destination?.route)
                        && showDebug
                    ) DebugToolbar(
                        navController = navController,
                        hideDebugClick = { showDebug = false }
                    )

                    LandingPageScreenDestination
                    DestinationsNavHost(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1F),
                        navGraph = NavGraphs.root,
                        engine = engine,
                        navController = navController,
                        dependenciesContainerBuilder = {
                            /**
                             * https://composedestinations.rafaelcosta.xyz/common-use-cases/providing-viewmodels
                             */

                            dependency(NavGraphs.landing) {
                                val parentEntry = remember(navBackStackEntry) {
                                    navController.safeGetBackStackEntry(NavGraphs.landing.route)
                                }
                                hiltViewModel<CreateIdentityViewModel>(parentEntry)
                            }

                            dependency(identityViewModel)
                            dependency(dialogViewModel)
                            dependency(bottomSheetViewModel)
                            dependency(deeplinkViewModel)
                        }
                    )
                }

                CloseableDialog(
                    navController = navController,
                    dialogViewModel = dialogViewModel
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        deeplinkViewModel.setDeeplink(intent)
    }
}