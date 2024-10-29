package com.positiveHelicopter.doobyplus.screens.watch

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.positiveHelicopter.doobyplus.utility.DoobyPreview

//https://player.twitch.tv/?autoplay=false&channel=michimochievee&parent=player.twitch.tv
//https://www.twitch.tv/embed/dooby3d/chat?parent=twitch.tv
@Composable
internal fun WatchScreen(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: WatchViewModel = hiltViewModel(),
    setOrientation: (Int) -> Unit = {},
    toggleBottomBarHidden: () -> Unit = {},
    hideSystemBars: () -> Unit = {},
    openTwitch: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    Box(modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopStart) {
        when(state) {
            is WatchState.Default -> TwitchPlayer(
                modifier = modifier.padding(innerPadding),
                toggleBottomBarHidden = toggleBottomBarHidden,
                setOrientation = setOrientation,
                enterFullScreen = viewModel::enterFullScreen,
                exitFullScreen = viewModel::exitFullScreen,
                openTwitch = openTwitch
            )
            is WatchState.FullScreen -> FullScreenTwitchPlayer(
                modifier = modifier,
                fullScreenView = (state as WatchState.FullScreen).view,
                setOrientation = setOrientation,
                hideSystemBars = hideSystemBars,
            )
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
internal fun TwitchPlayer(
    modifier: Modifier = Modifier,
    toggleBottomBarHidden: () -> Unit = {},
    setOrientation: (Int) -> Unit = {},
    enterFullScreen: (View?) -> Unit = {},
    exitFullScreen: () -> Unit = {},
    openTwitch: () -> Unit = {}
) {
    setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    val context = LocalContext.current
    val playerHeight = 300.dp
    val playerHeightPx = with(LocalDensity.current) { (playerHeight - 35.dp).toPx().toInt() }
    val html = "<iframe id=\"player\" src=\"https://player.twitch.tv/?autoplay=false&channel=singsing&parent=player.twitch.tv\" height=\"$playerHeightPx\" width=\"100%\"></iframe>"
    AndroidView(
        modifier = modifier.fillMaxWidth().height(playerHeight),
        factory = {
            WebView(context).apply {
                webViewClient = object: WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        openTwitch()
                        return true
                    }
                }
                webChromeClient = object: WebChromeClient() {
                    override fun onHideCustomView() {
                        toggleBottomBarHidden()
                        exitFullScreen()
                    }

                    override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                        toggleBottomBarHidden()
                        enterFullScreen(view)
                    }
                }
                settings.useWideViewPort = true
                settings.loadWithOverviewMode = true
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.cacheMode = WebSettings.LOAD_NO_CACHE

                loadDataWithBaseURL("https://player.twitch.tv/", html, "text/html", "UTF-8", null)
            }
        },
        update = {}
    )
}

@Composable
internal fun FullScreenTwitchPlayer(
    modifier: Modifier = Modifier,
    fullScreenView: View? = null,
    setOrientation: (Int) -> Unit = {},
    hideSystemBars: () -> Unit = {}
) {
    fullScreenView ?: return
    setOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE)
    hideSystemBars()
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { fullScreenView },
        update = {}
    )
}

@DoobyPreview
@Composable
internal fun WatchScreenPreview() {
    WatchScreen()
}