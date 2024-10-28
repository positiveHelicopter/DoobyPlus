package com.positiveHelicopter.doobyplus.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.positiveHelicopter.doobyplus.screens.main.navigation.mainScreen
import com.positiveHelicopter.doobyplus.screens.main.navigation.navigateToMain
import com.positiveHelicopter.doobyplus.screens.splash.navigation.SPLASH_ROUTE
import com.positiveHelicopter.doobyplus.screens.splash.navigation.splashScreen

@Composable
internal fun DoobApp(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = SPLASH_ROUTE,
        modifier = modifier
    ) {
        splashScreen(navController::navigateToMain)
        mainScreen()
    }
}