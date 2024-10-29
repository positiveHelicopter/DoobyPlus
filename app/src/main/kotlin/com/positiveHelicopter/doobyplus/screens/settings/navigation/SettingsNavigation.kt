package com.positiveHelicopter.doobyplus.screens.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.positiveHelicopter.doobyplus.screens.settings.SettingsScreen

internal const val SETTINGS_ROUTE = "settings"

internal fun NavController.navigateToSettings() {
    navigate(SETTINGS_ROUTE) { popBackStack() }
}

internal fun NavGraphBuilder.settingsScreen(setOrientation: (Int) -> Unit = {}) {
    composable(SETTINGS_ROUTE) {
        SettingsScreen(setOrientation = setOrientation)
    }
}