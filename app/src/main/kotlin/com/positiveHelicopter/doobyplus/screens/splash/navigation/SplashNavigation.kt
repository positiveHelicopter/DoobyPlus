package com.positiveHelicopter.doobyplus.screens.splash.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.positiveHelicopter.doobyplus.screens.splash.DoobSplashScreen

internal const val SPLASH_ROUTE = "splash"

internal fun NavGraphBuilder.splashScreen(endSplash: () -> Unit) {
    composable(SPLASH_ROUTE) {
        DoobSplashScreen(endSplash = endSplash)
    }
}