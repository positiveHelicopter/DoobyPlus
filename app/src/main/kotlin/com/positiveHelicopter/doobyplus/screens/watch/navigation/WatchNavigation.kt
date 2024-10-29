package com.positiveHelicopter.doobyplus.screens.watch.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.positiveHelicopter.doobyplus.screens.watch.WatchScreen

internal const val WATCH_ROUTE = "watch"

internal fun NavController.navigateToWatch() {
    navigate(WATCH_ROUTE) { popBackStack() }
}

internal fun NavGraphBuilder.watchScreen(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    setOrientation: (Int) -> Unit = {},
    toggleBottomBarHidden: () -> Unit = {},
    hideSystemBars: () -> Unit = {},
    openTwitch: () -> Unit = {}
) {
    composable(WATCH_ROUTE) {
        WatchScreen(
            innerPadding = innerPadding,
            setOrientation = setOrientation,
            toggleBottomBarHidden = toggleBottomBarHidden,
            hideSystemBars = hideSystemBars,
            openTwitch = openTwitch
        )
    }
}