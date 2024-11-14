package com.positiveHelicopter.doobyplus.screens.main.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.positiveHelicopter.doobyplus.screens.main.DoobMainApp

internal const val MAIN_ROUTE = "main"

internal fun NavController.navigateToMain() {
    navigate(MAIN_ROUTE) {
        popUpTo(MAIN_ROUTE) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

internal fun NavGraphBuilder.mainScreen(
    setOrientation: (Int) -> Unit = {},
    hideSystemBars: () -> Unit = {},
    openTwitch: ((String, String) -> Unit) -> Unit = {},
    launchCustomTab: (String, Boolean, (String, String) -> Unit) -> Unit = { _ ,_, _ -> },
    askNotificationPermission: ((() -> Unit) -> Unit) -> Unit = {}
) {
    composable(
        MAIN_ROUTE,
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() },
        popEnterTransition = { fadeIn() },
        popExitTransition = { fadeOut() }
    ) {
        DoobMainApp(
            setOrientation = setOrientation,
            hideSystemBars = hideSystemBars,
            openTwitch = openTwitch,
            launchCustomTab = launchCustomTab,
            askNotificationPermission = askNotificationPermission
        )
    }
}