package com.unfinished.dsnp_wallet_kotlin.ui

import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.annotation.RootNavGraph

@RootNavGraph(start = true)
@NavGraph
annotation class SplashNavGraph(
    val start: Boolean = true
)

@RootNavGraph
@NavGraph
annotation class LandingNavGraph(
    val start: Boolean = false
)

@RootNavGraph
@NavGraph
annotation class MainNavGraph(
    val start: Boolean = true
)

@RootNavGraph
@NavGraph
annotation class BottomBarNavGraph(
    val start: Boolean = false
)

@RootNavGraph
@NavGraph
annotation class DebugNavGraph(
    val start: Boolean = false
)