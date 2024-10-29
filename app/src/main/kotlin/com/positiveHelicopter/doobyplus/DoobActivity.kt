package com.positiveHelicopter.doobyplus

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.positiveHelicopter.doobyplus.screens.DoobApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@SuppressLint("SourceLockedOrientationActivity")
class DoobActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.setFlags(FLAG_LAYOUT_NO_LIMITS, FLAG_LAYOUT_NO_LIMITS)
        setContent {
            DoobApp(
                setOrientation = ::setOrientation,
                hideSystemBars = ::hideSystemBars,
                openTwitch = ::openTwitch
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

    private fun openTwitch() {
        try {
            val uri = Uri.parse("twitch://stream/dooby3d")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            //show error message
        }
    }
}