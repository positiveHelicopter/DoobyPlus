package com.positiveHelicopter.doobyplus.screens.watch

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
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
        contentAlignment = Alignment.Center) {
        when(state) {
            is WatchState.Default -> TwitchPlayer(
                modifier = modifier,
                innerPadding = innerPadding,
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

@Composable
internal fun TwitchPlayer(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(0.dp),
    toggleBottomBarHidden: () -> Unit = {},
    setOrientation: (Int) -> Unit = {},
    enterFullScreen: (View?) -> Unit = {},
    exitFullScreen: () -> Unit = {},
    openTwitch: () -> Unit = {}
) {
    setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    val streamHeight = 300.dp
    val streamHeightPx = with(LocalDensity.current) { (streamHeight - 35.dp).toPx().toInt() }
    val streamHtml = "<iframe id=\"player\" src=\"https://player.twitch.tv/?autoplay=false&channel=singsing&parent=player.twitch.tv\" height=\"$streamHeightPx\" width=\"100%\">"
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val chatHeight = screenHeight - streamHeight - innerPadding.calculateBottomPadding()
    val chatHeightPx = with(LocalDensity.current) { (chatHeight - 60.dp).toPx().toInt() }
    val chatHtml = "<iframe id=\"player\" src=\"https://www.twitch.tv/embed/singsing/chat?darkpopout&parent=twitch.tv\" height=\"$chatHeightPx\" width=\"100%\">"
    Column(modifier = modifier.padding(innerPadding),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        TwitchWebView(
            modifier = Modifier,
            toggleBottomBarHidden = toggleBottomBarHidden,
            enterFullScreen = enterFullScreen,
            exitFullScreen = exitFullScreen,
            openTwitch = openTwitch,
            htmlString = streamHtml,
            playerHeight = streamHeight
        )
        TwitchWebView(
            modifier = Modifier,
            toggleBottomBarHidden = toggleBottomBarHidden,
            enterFullScreen = enterFullScreen,
            exitFullScreen = exitFullScreen,
            openTwitch = openTwitch,
            htmlString = chatHtml,
            playerHeight = chatHeight
        )
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
internal fun TwitchWebView(
    modifier: Modifier = Modifier,
    toggleBottomBarHidden: () -> Unit = {},
    enterFullScreen: (View?) -> Unit = {},
    exitFullScreen: () -> Unit = {},
    openTwitch: () -> Unit = {},
    htmlString: String = "",
    playerHeight: Dp = 300.dp
) {
    val context = LocalContext.current
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

                    override fun onPageFinished(view: WebView?, url: String?) {
                        loadUrl("javascript:document.body.style.setProperty(\"background-color\", \"black\");")
                        super.onPageFinished(view, url)
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

                loadDataWithBaseURL("https://player.twitch.tv/", htmlString, "text/html", "UTF-8", null)
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