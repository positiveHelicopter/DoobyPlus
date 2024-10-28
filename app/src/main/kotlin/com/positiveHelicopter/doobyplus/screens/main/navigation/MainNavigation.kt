package com.positiveHelicopter.doobyplus.screens.main.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.positiveHelicopter.doobyplus.screens.main.DoobScreen

internal const val MAIN_ROUTE = "main"

internal fun NavController.navigateToMain() {
    navigate(MAIN_ROUTE)
}

internal fun NavGraphBuilder.mainScreen() {
    composable(MAIN_ROUTE) {
        DoobScreen()
    }
}