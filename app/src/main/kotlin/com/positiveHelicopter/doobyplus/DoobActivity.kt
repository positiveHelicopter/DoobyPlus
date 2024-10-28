package com.positiveHelicopter.doobyplus

import android.os.Bundle
import android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.positiveHelicopter.doobyplus.screens.DoobApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DoobActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.setFlags(FLAG_LAYOUT_NO_LIMITS, FLAG_LAYOUT_NO_LIMITS)
        setContent {
            DoobApp()
        }
    }
}