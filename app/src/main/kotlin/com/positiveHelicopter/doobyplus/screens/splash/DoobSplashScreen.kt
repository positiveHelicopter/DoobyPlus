package com.positiveHelicopter.doobyplus.screens.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.gif.onAnimationEnd
import coil3.gif.repeatCount
import coil3.request.ImageRequest
import com.positiveHelicopter.doobyplus.R
import com.positiveHelicopter.doobyplus.utility.DoobyPreview

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
        val request = ImageRequest.Builder(context)
            .data(R.raw.jerboa_launch)
            .repeatCount(0)
            .onAnimationEnd(endSplash)
            .build()
        AsyncImage(
            modifier = modifier
            .height(config.screenHeightDp.dp + topInset + botInset),
            model = request,
            contentDescription = "",
            contentScale = ContentScale.FillHeight,
            clipToBounds = true
        )
    }
}

@DoobyPreview
@Composable
internal fun DoobSplashScreenPreview() {
    DoobSplashScreen()
}