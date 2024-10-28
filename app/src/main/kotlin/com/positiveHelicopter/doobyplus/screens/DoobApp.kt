package com.positiveHelicopter.doobyplus.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.positiveHelicopter.doobyplus.screens.main.navigation.MAIN_ROUTE
import com.positiveHelicopter.doobyplus.screens.main.navigation.mainScreen

@Composable
internal fun DoobApp(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = MAIN_ROUTE,
        modifier = modifier
    ) {
        mainScreen()
    }
}