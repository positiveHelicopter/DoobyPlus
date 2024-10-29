package com.positiveHelicopter.doobyplus.screens.watch.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.positiveHelicopter.doobyplus.screens.watch.WatchScreen

internal const val WATCH_ROUTE = "watch"

internal fun NavController.navigateToWatch() {
    navigate(WATCH_ROUTE) { popBackStack() }
}

internal fun NavGraphBuilder.watchScreen(setOrientation: (Int) -> Unit = {}) {
    composable(WATCH_ROUTE) {
        WatchScreen(setOrientation = setOrientation)
    }
}