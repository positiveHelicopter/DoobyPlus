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
import com.google.firebase.messaging.FirebaseMessaging

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
        getCurrentFirebaseToken()
    }

    private fun setOrientation(orientation: Int) {
        requestedOrientation = orientation
    }

    private fun hideSystemBars() {
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }

    private fun openTwitch() {
        try {
            val uri = Uri.parse("twitch://stream/dooby3d")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            //show error message
        }
    }

    private fun launchCustomTab(url: String) {
        try {
            val intent = CustomTabsIntent.Builder().build()
            intent.intent.`package` = "com.android.chrome"
            intent.launchUrl(this, Uri.parse(url))
        } catch (e: Exception) {
            //show error message
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

    private fun getCurrentFirebaseToken() {
        FirebaseMessaging.getInstance().apply {
            token.addOnCompleteListener { task ->
                if(!task.isSuccessful) {
                    return@addOnCompleteListener
                }
                val token = task.result
                println("Token: $token")
            }
            subscribeToTopic("twitch_stream_online").addOnCompleteListener {
                if (it.isSuccessful) {
                    println("Subscribed to twitch_stream_online")
                }
            }
            subscribeToTopic("twitter_post").addOnCompleteListener {
                if (it.isSuccessful) {
                    println("Subscribed to twitter_post")
                }
            }
        }
    }
}