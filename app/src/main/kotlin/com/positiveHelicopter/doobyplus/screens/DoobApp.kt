package com.positiveHelicopter.doobyplus.screens

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.positiveHelicopter.doobyplus.R
import com.positiveHelicopter.doobyplus.screens.settings.navigation.SETTINGS_ROUTE
import com.positiveHelicopter.doobyplus.screens.settings.navigation.navigateToSettings
import com.positiveHelicopter.doobyplus.screens.settings.navigation.settingsScreen
import com.positiveHelicopter.doobyplus.screens.socials.navigation.SOCIALS_ROUTE
import com.positiveHelicopter.doobyplus.screens.socials.navigation.navigateToSocials
import com.positiveHelicopter.doobyplus.screens.socials.navigation.socialsScreen
import com.positiveHelicopter.doobyplus.screens.watch.navigation.WATCH_ROUTE
import com.positiveHelicopter.doobyplus.screens.watch.navigation.navigateToWatch
import com.positiveHelicopter.doobyplus.screens.watch.navigation.watchScreen
import com.positiveHelicopter.doobyplus.ui.ErrorDialog
import com.positiveHelicopter.doobyplus.utility.DoobyPreview

@Composable
internal fun DoobApp(
    modifier: Modifier = Modifier,
    setOrientation: (Int) -> Unit = {},
    hideSystemBars: () -> Unit = {},
    openTwitch: ((String, String) -> Unit) -> Unit = {},
    launchCustomTab: (String, (String, String) -> Unit) -> Unit = { _ ,_ -> },
    askNotificationPermission: ((() -> Unit) -> Unit) -> Unit = {}
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val background = painterResource(R.drawable.doob_background)
    var hideBottomBar by rememberSaveable { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorTitle by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf("") }
    Scaffold(modifier = modifier,
        bottomBar = { DoobBottomBar(
            navBackStackEntry = navBackStackEntry,
            navigateToWatch = navController::navigateToWatch,
            navigateToSocials = navController::navigateToSocials,
            navigateToSettings = navController::navigateToSettings,
            isHidden = hideBottomBar
        ) },
        containerColor = colorResource(id = R.color.backgroundColor)) { innerPadding ->
        if (showErrorDialog) {
            ErrorDialog(title = errorTitle, text = errorText) {
                showErrorDialog = false
            }
        }
        Image(
            painter = background,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillWidth
        )
        NavHost(
            modifier = modifier.fillMaxSize(),
            navController = navController,
            startDestination = SOCIALS_ROUTE,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            watchScreen(
                innerPadding = innerPadding,
                setOrientation = setOrientation,
                toggleBottomBarHidden = { hideBottomBar = !hideBottomBar },
                hideSystemBars = hideSystemBars,
                openTwitch = {
                    openTwitch { title, text ->
                        errorTitle = title
                        errorText = text
                        showErrorDialog = true
                    }
                }
            )
            socialsScreen(
                innerPadding = innerPadding,
                setOrientation = setOrientation,
                launchCustomTab = { url ->
                    launchCustomTab(url) { title, text ->
                        errorTitle = title
                        errorText = text
                        showErrorDialog = true
                    }
                },
                askNotificationPermission = askNotificationPermission
            )
            settingsScreen(
                innerPadding = innerPadding,
                setOrientation = setOrientation
            )
        }
    }
}

@Composable
internal fun DoobBottomBar(
    modifier: Modifier = Modifier,
    navBackStackEntry: NavBackStackEntry?,
    navigateToWatch: () -> Unit = {},
    navigateToSocials: () -> Unit = {},
    navigateToSettings: () -> Unit = {},
    isHidden: Boolean = false
) {
    if (isHidden) return
    val items = listOf(
        DoobNavigationBarItem(
            unselectedIcon = Icons.Outlined.Home,
            selectedIcon = Icons.Filled.Home,
            contentDescription = WATCH_ROUTE,
            route = WATCH_ROUTE
        ),
        DoobNavigationBarItem(
            unselectedIcon = Icons.Outlined.Person,
            selectedIcon = Icons.Filled.Person,
            contentDescription = SOCIALS_ROUTE,
            route = SOCIALS_ROUTE
        ),
        DoobNavigationBarItem(
            unselectedIcon = Icons.Outlined.Settings,
            selectedIcon = Icons.Filled.Settings,
            contentDescription = SETTINGS_ROUTE,
            route = SETTINGS_ROUTE
        ),
    )
    val route = navBackStackEntry?.destination?.route
    NavigationBar(
        modifier = modifier,
        containerColor = colorResource(R.color.colorPrimary)
    ) {
        items.forEach {
            val selected = route == it.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected)
                    when(it.route) {
                        WATCH_ROUTE -> navigateToWatch()
                        SOCIALS_ROUTE -> navigateToSocials()
                        SETTINGS_ROUTE -> navigateToSettings()
                        else -> navigateToWatch()
                    }
                },
                colors = doobNavigationBarItemColors(),
                icon = {
                    Icon(
                        if(selected) it.selectedIcon else it.unselectedIcon,
                        contentDescription = "Settings"
                    )
                }
            )
        }
    }
}

internal fun doobNavigationBarItemColors() =
    NavigationBarItemColors(
        selectedIconColor = Color.Unspecified,
        selectedTextColor = Color.Unspecified,
        selectedIndicatorColor = Color.Unspecified,
        unselectedIconColor = Color.Unspecified,
        unselectedTextColor = Color.Unspecified,
        disabledIconColor = Color.Unspecified,
        disabledTextColor = Color.Unspecified
    )

data class DoobNavigationBarItem(
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector,
    val contentDescription: String,
    val route: String
)

@DoobyPreview
@Composable
internal fun DoobAppPreview() {
    DoobApp()
}