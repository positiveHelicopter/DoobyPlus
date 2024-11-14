package com.positiveHelicopter.doobyplus.screens.settings.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.positiveHelicopter.doobyplus.screens.settings.SettingsScreen
import com.positiveHelicopter.doobyplus.screens.socials.navigation.SOCIALS_ROUTE

internal const val SETTINGS_ROUTE = "settings"

internal fun NavController.navigateToSettings() {
    navigate(SETTINGS_ROUTE) {
        popUpTo(SOCIALS_ROUTE) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

internal fun NavGraphBuilder.settingsScreen(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    setOrientation: (Int) -> Unit = {},
    launchCustomTab: (String, Boolean) -> Unit = { _, _ -> },
    askNotificationPermission: ((() -> Unit) -> Unit) -> Unit = {}
) {
    composable(SETTINGS_ROUTE,
        enterTransition = { slideInHorizontally(initialOffsetX = {it}) },
        popEnterTransition = { slideInHorizontally(initialOffsetX = {it}) },
        exitTransition = { slideOutHorizontally(targetOffsetX = {it}) },
        popExitTransition = { slideOutHorizontally(targetOffsetX = {it}) }
    ) {
        SettingsScreen(
            innerPadding = innerPadding,
            setOrientation = setOrientation,
            launchCustomTab = launchCustomTab,
            askNotificationPermission = askNotificationPermission
        )
    }
}