package com.positiveHelicopter.doobyplus.screens.splash

import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.positiveHelicopter.doobyplus.R

@Composable
internal fun DoobSplashScreen(
    modifier: Modifier = Modifier,
    endSplash: () -> Unit = {}
) {
    val context = LocalContext.current
    val config = LocalConfiguration.current
    val topInset = with(LocalDensity.current) {
        WindowInsets.Companion.systemBars.getTop(LocalDensity.current).toDp()
    }
    val botInset = with(LocalDensity.current) {
        WindowInsets.Companion.systemBars.getBottom(LocalDensity.current).toDp()
    }
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Companion.TopStart
    ) {
        AndroidView(modifier = modifier
            .scale(scaleX = 1.7f, scaleY = 1.0f)
            .height(config.screenHeightDp.dp + topInset + botInset),
            factory = {
                val videoView = VideoView(context)
                val videoUri =
                    Uri.parse("android.resource://${context.packageName}/${R.raw.jerboa_launch}")
                videoView.setVideoURI(videoUri)
                videoView.setOnPreparedListener {
                    it.start()
                }
                videoView.setOnCompletionListener {
                    endSplash()
                }
                videoView
            },
            update = {})
    }
}

@Preview(widthDp = 720, heightDp = 1280)
@Composable
internal fun DoobSplashScreenPreview() {
    DoobSplashScreen()
}