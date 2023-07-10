package com.unfinished.dsnp_wallet_kotlin.root

import androidx.navigation.NavController
import com.unfinished.common.resources.ContextManager

class NavigationHolder(val contextManager: ContextManager) {

    var navController: NavController? = null
        private set

    fun attach(navController: NavController) {
        this.navController = navController
    }

    fun detach() {
        navController = null
    }
}

fun NavigationHolder.executeBack() {
    val popped = navController!!.popBackStack()

    if (!popped) {
        contextManager.getActivity()!!.finish()
    }
}
