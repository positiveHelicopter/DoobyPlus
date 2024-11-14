package com.positiveHelicopter.doobyplus.screens

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.positiveHelicopter.doobyplus.screens.main.navigation.MAIN_ROUTE
import com.positiveHelicopter.doobyplus.screens.main.navigation.mainScreen
import com.positiveHelicopter.doobyplus.utility.DoobyPreview

@Composable
internal fun DoobApp(
    modifier: Modifier = Modifier,
    setOrientation: (Int) -> Unit = {},
    hideSystemBars: () -> Unit = {},
    openTwitch: ((String, String) -> Unit) -> Unit = {},
    launchCustomTab: (String, Boolean, (String, String) -> Unit) -> Unit = { _ ,_, _ -> },
    askNotificationPermission: ((() -> Unit) -> Unit) -> Unit = {}
) {
    val navController = rememberNavController()
    NavHost(
        modifier = modifier.fillMaxSize(),
        navController = navController,
        startDestination = MAIN_ROUTE,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        mainScreen(
            setOrientation = setOrientation,
            hideSystemBars = hideSystemBars,
            openTwitch = openTwitch,
            launchCustomTab = launchCustomTab,
            askNotificationPermission = askNotificationPermission
        )
    }
}

@DoobyPreview
@Composable
internal fun DoobAppPreview() {
    DoobApp()
}