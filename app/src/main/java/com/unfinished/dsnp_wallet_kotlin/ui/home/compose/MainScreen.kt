package com.unfinished.dsnp_wallet_kotlin.ui.home.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.dependency
import com.unfinished.dsnp_wallet_kotlin.ui.MainNavGraph
import com.unfinished.dsnp_wallet_kotlin.ui.NavGraphs
import com.unfinished.dsnp_wallet_kotlin.ui.bottombar.BottomBar
import com.unfinished.dsnp_wallet_kotlin.ui.destinations.DirectionDestination
import com.unfinished.dsnp_wallet_kotlin.ui.home.viewmmodel.IdentityViewModel
import com.unfinished.dsnp_wallet_kotlin.ui.recovery.viewmodel.RecoveryPhraseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@MainNavGraph
@Destination
@Composable
fun MainScreen(
    navigator: DestinationsNavigator,
    identityViewModel: IdentityViewModel,
    directionRoute: String? = null
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomBar(navController)
        }
    ) {
        DestinationsNavHost(
            navController = navController,
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            navGraph = NavGraphs.root,
            startRoute = NavGraphs.bottomBar,
            dependenciesContainerBuilder = {
                /**
                 * https://composedestinations.rafaelcosta.xyz/common-use-cases/providing-viewmodels
                 */
                dependency(identityViewModel)

                dependency(NavGraphs.recovery) {
                    val parentEntry = remember(navBackStackEntry) {
                        navController.getBackStackEntry(NavGraphs.recovery.route)
                    }
                    hiltViewModel<RecoveryPhraseViewModel>(parentEntry)
                }
            }
        )
    }

    LaunchedEffect(
        key1 = directionRoute,
        block = {
            if (directionRoute != null) {
                navController.navigate(directionRoute)
            }
        }
    )
}