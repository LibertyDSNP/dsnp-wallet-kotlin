package com.unfinished.dsnp_wallet_kotlin.ui.splash.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.unfinished.dsnp_wallet_kotlin.ui.NavGraphs
import com.unfinished.dsnp_wallet_kotlin.ui.SplashNavGraph
import com.unfinished.dsnp_wallet_kotlin.ui.splash.SplashViewModel
import com.unfinished.uikit.MainColors
import com.unfinished.uikit.MainTheme
import com.unfinished.uikit.components.Logo

@SplashNavGraph
@Destination
@Composable
fun SplashScreen(
    navigator: DestinationsNavigator,
    splashViewModel: SplashViewModel = hiltViewModel()
) {
    val uiState = splashViewModel.uiStateFLow.collectAsState()
    SplashScreen()

    val route: String? = when (uiState.value) {
        is SplashViewModel.HeadToLanding -> NavGraphs.landing.route
        is SplashViewModel.HeadToCreatePincode -> TODO()
        is SplashViewModel.HeadToCheckPincode -> TODO()
        else -> null
    }

    if (!route.isNullOrBlank()) navigator.navigate(
        route,
        builder = {
            popUpTo(
                route = NavGraphs.splash.route,
                popUpToBuilder = {
                    inclusive = true
                }
            )
        }
    )
}

@Composable
fun SplashScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MainColors.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Logo()
    }
}

@Preview
@Composable
private fun SampleSplashScreen() {
    MainTheme {
        SplashScreen()
    }
}