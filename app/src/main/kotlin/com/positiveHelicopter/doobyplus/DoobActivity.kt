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

@AndroidEntryPoint
@SuppressLint("SourceLockedOrientationActivity")
class DoobActivity: ComponentActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { _: Boolean -> }

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
        onError: (String, String) -> Unit
    ) {
        try {
            val intent = CustomTabsIntent.Builder().build()
            intent.intent.`package` = "com.android.chrome"
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