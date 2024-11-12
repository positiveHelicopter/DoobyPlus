package com.positiveHelicopter.doobyplus.screens.socials.navigation

import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.positiveHelicopter.doobyplus.screens.socials.SocialsScreen

internal const val SOCIALS_ROUTE = "socials"

internal fun NavController.navigateToSocials() {
    navigate(SOCIALS_ROUTE) {
        popUpTo(SOCIALS_ROUTE) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

internal fun NavGraphBuilder.socialsScreen(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    setOrientation: (Int) -> Unit = {},
    launchCustomTab: (String, Boolean) -> Unit = { _, _ -> },
    askNotificationPermission: ((() -> Unit) -> Unit) -> Unit = {}
) {
    composable(
        SOCIALS_ROUTE,
        enterTransition = null,
        exitTransition = { fadeOut() },
        popEnterTransition = null,
        popExitTransition = { fadeOut() }
    ) {
        SocialsScreen(
            innerPadding = innerPadding,
            setOrientation = setOrientation,
            launchCustomTab = launchCustomTab,
            askNotificationPermission = askNotificationPermission
        )
    }
}