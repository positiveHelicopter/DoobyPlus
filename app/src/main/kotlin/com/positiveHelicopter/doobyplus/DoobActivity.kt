package com.positiveHelicopter.doobyplus

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.positiveHelicopter.doobyplus.screens.DoobApp
import dagger.hilt.android.AndroidEntryPoint
import android.Manifest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsClient
import com.positiveHelicopter.doobyplus.datastore.PreferenceDataSource
import com.positiveHelicopter.doobyplus.network.FireStoreUpdateManager
import com.positiveHelicopter.doobyplus.utility.di.Dispatcher
import com.positiveHelicopter.doobyplus.utility.di.DispatcherType.IO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
@SuppressLint("SourceLockedOrientationActivity")
class DoobActivity: ComponentActivity() {
    @Inject @Dispatcher(IO) lateinit var ioDispatcher: CoroutineDispatcher
    @Inject lateinit var preferenceDataSource: PreferenceDataSource
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { _: Boolean ->
        CoroutineScope(ioDispatcher).launch {
            preferenceDataSource.updateIsFirstTimeNotification(false)
        }
    }
    @Inject lateinit var fireStoreUpdateManager: FireStoreUpdateManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.setFlags(FLAG_LAYOUT_NO_LIMITS, FLAG_LAYOUT_NO_LIMITS)
        setContent {
            DoobApp(
                setOrientation = ::setOrientation,
                hideSystemBars = ::hideSystemBars,
                openTwitch = ::openTwitch,
                launchCustomTab = ::launchCustomTab,
                askNotificationPermission = ::askNotificationPermission
            )
        }
        fireStoreUpdateManager.listenForUpdates()
    }

    override fun onDestroy() {
        fireStoreUpdateManager.unregisterListeners()
        super.onDestroy()
    }

    private fun setOrientation(orientation: Int) {
        requestedOrientation = orientation
    }

    private fun hideSystemBars() {
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }

    private fun openTwitch(
        onError: (String, String) -> Unit
    ) {
        try {
            val uri = Uri.parse("twitch://stream/dooby3d")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            onError(
                getString(R.string.error_title_open_twitch),
                getString(R.string.error_message_open_twitch)
            )
        }
    }

    private fun launchCustomTab(
        url: String,
        shouldRedirectUrl: Boolean,
        onError: (String, String) -> Unit
    ) {
        try {
            val intent = CustomTabsIntent.Builder()
                .setColorScheme(CustomTabsIntent.COLOR_SCHEME_DARK)
                .setColorSchemeParams(CustomTabsIntent.COLOR_SCHEME_DARK,
                    CustomTabColorSchemeParams.Builder().build())
                .build()
            if (!shouldRedirectUrl) {
                val packageName = CustomTabsClient.getPackageName(this, null)
                if (packageName != null) {
                    intent.intent.`package` = packageName
                }
            }
            intent.launchUrl(this, Uri.parse(url))
        } catch (e: Exception) {
            onError(
                getString(R.string.error_title_custom_tab),
                getString(R.string.error_message_custom_tab)
            )
        }
    }

    private fun askNotificationPermission(displayDialog: (() -> Unit) -> Unit) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED
        ) return
        val requestPermission = {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        displayDialog(requestPermission)
    }
}