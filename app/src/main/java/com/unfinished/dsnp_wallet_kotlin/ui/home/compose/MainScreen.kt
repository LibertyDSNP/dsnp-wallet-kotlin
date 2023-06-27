package com.unfinished.dsnp_wallet_kotlin.ui.home.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.dependency
import com.unfinished.dsnp_wallet_kotlin.ui.MainNavGraph
import com.unfinished.dsnp_wallet_kotlin.ui.NavGraphs
import com.unfinished.dsnp_wallet_kotlin.ui.bottombar.BottomBar
import com.unfinished.dsnp_wallet_kotlin.ui.home.viewmmodel.IdentityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@MainNavGraph
@Destination
@Composable
fun MainScreen(
    navigator: DestinationsNavigator,
    identityViewModel: IdentityViewModel = hiltViewModel()
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
            }
        )
    }
}