package com.positiveHelicopter.doobyplus.screens.splash

import android.annotation.SuppressLint
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint

import android.os.Bundle
import android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.positiveHelicopter.doobyplus.DoobActivity

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class DoobSplashActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.setFlags(FLAG_LAYOUT_NO_LIMITS, FLAG_LAYOUT_NO_LIMITS)
        setContent {
            DoobSplashScreen(
                endSplash = {
                    val intent = Intent(this, DoobActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.accelerate_interpolator)
                }
            )
        }
    }
}