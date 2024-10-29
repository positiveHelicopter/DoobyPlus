package com.positiveHelicopter.doobyplus.screens.socials.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.positiveHelicopter.doobyplus.screens.socials.SocialsScreen

internal const val SOCIALS_ROUTE = "socials"

internal fun NavController.navigateToSocials() {
    navigate(SOCIALS_ROUTE) { popBackStack() }
}

internal fun NavGraphBuilder.socialsScreen() {
    composable(SOCIALS_ROUTE) {
        SocialsScreen()
    }
}