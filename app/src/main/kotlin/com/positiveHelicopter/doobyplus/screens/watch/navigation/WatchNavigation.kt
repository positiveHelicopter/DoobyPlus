package com.positiveHelicopter.doobyplus.screens.watch.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.positiveHelicopter.doobyplus.screens.socials.navigation.SOCIALS_ROUTE
import com.positiveHelicopter.doobyplus.screens.watch.WatchScreen

internal const val WATCH_ROUTE = "watch"

internal fun NavController.navigateToWatch() {
    navigate(WATCH_ROUTE) {
        popUpTo(SOCIALS_ROUTE) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

internal fun NavGraphBuilder.watchScreen(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    setOrientation: (Int) -> Unit = {},
    toggleBottomBarHidden: (Boolean) -> Unit = {},
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