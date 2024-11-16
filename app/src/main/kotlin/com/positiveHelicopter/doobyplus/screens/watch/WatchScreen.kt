package com.positiveHelicopter.doobyplus.screens.watch

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.positiveHelicopter.doobyplus.R
import com.positiveHelicopter.doobyplus.utility.DoobyPreview

@Composable
internal fun WatchScreen(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: WatchViewModel = hiltViewModel(),
    setOrientation: (Int) -> Unit = {},
    toggleBottomBarHidden: (Boolean) -> Unit = {},
    toggleFabHidden: (Boolean) -> Unit = {},
    hideSystemBars: () -> Unit = {},
    openExternalApp: (String) -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    WatchScreen(
        modifier = modifier,
        state = state,
        innerPadding = innerPadding,
        setOrientation = setOrientation,
        toggleBottomBarHidden = toggleBottomBarHidden,
        toggleFabHidden = toggleFabHidden,
        hideSystemBars = hideSystemBars,
        openExternalApp = openExternalApp,
        enterFullScreen = viewModel::enterFullScreen,
        exitFullScreen = viewModel::exitFullScreen
    )
}

@Composable
internal fun WatchScreen(
    modifier: Modifier = Modifier,
    state: WatchState,
    innerPadding: PaddingValues = PaddingValues(0.dp),
    setOrientation: (Int) -> Unit = {},
    toggleBottomBarHidden: (Boolean) -> Unit = {},
    toggleFabHidden: (Boolean) -> Unit = {},
    hideSystemBars: () -> Unit = {},
    openExternalApp: (String) -> Unit = {},
    enterFullScreen: (View?) -> Unit = {},
    exitFullScreen: () -> Unit
) {
    val playerList = listOf(PlayerType.TWITCH_PLAYER, PlayerType.YOUTUBE_PLAYER)
    Box(modifier = modifier.fillMaxSize().background(colorResource(R.color.color_black_faded)),
        contentAlignment = Alignment.Center) {
        when(state) {
            is WatchState.Default -> {}
            is WatchState.Success -> {
                state.data.view?.apply {
                    FullScreenPlayer(
                        modifier = modifier,
                        fullScreenView = this,
                        setOrientation = setOrientation,
                        hideSystemBars = hideSystemBars,
                    )
                } ?: run {
                    if (state.data.userPreference.bottomNavigationExpandedState)
                        toggleBottomBarHidden(false)
                    else toggleBottomBarHidden(true)
                    LazyColumn(modifier = modifier.padding(innerPadding),
                        verticalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        items(playerList.size) { index ->
                            Player(
                                modifier = modifier,
                                type = playerList[index],
                                toggleBottomBarHidden = toggleBottomBarHidden,
                                toggleFabHidden = toggleFabHidden,
                                setOrientation = setOrientation,
                                enterFullScreen = enterFullScreen,
                                exitFullScreen = exitFullScreen,
                                openExternalApp = openExternalApp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun Player(
    modifier: Modifier = Modifier,
    type: PlayerType = PlayerType.TWITCH_PLAYER,
    toggleBottomBarHidden: (Boolean) -> Unit = {},
    toggleFabHidden: (Boolean) -> Unit = {},
    setOrientation: (Int) -> Unit = {},
    enterFullScreen: (View?) -> Unit = {},
    exitFullScreen: () -> Unit = {},
    openExternalApp: (String) -> Unit = {}
) {
    setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    val streamHeight = 300.dp
    val streamHeightPx = with(LocalDensity.current) { (streamHeight - 35.dp).toPx().toInt() }
    val embeddedUrl = when(type) {
        PlayerType.TWITCH_PLAYER -> "https://player.twitch.tv/?autoplay=false&channel=dooby3d&parent=player.twitch.tv"
        PlayerType.YOUTUBE_PLAYER -> "https://www.youtube.com/embed/live_stream?channel=UC6T7TJZbW6nO-qsc5coo8Pg&origin=http://youtube.com"
    }
    val streamHtml = "<iframe id=\"player\" src=\"$embeddedUrl\" height=\"$streamHeightPx\" width=\"100%\">"
    WebViewPlayer(
        modifier = modifier,
        type = type,
        toggleBottomBarHidden = toggleBottomBarHidden,
        toggleFabHidden = toggleFabHidden,
        enterFullScreen = enterFullScreen,
        exitFullScreen = exitFullScreen,
        openExternalApp = openExternalApp,
        htmlString = streamHtml,
        playerHeight = streamHeight
    )
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
internal fun WebViewPlayer(
    modifier: Modifier = Modifier,
    type: PlayerType = PlayerType.TWITCH_PLAYER,
    toggleBottomBarHidden: (Boolean) -> Unit = {},
    toggleFabHidden: (Boolean) -> Unit = {},
    enterFullScreen: (View?) -> Unit = {},
    exitFullScreen: () -> Unit = {},
    openExternalApp: (String) -> Unit = {},
    htmlString: String = "",
    playerHeight: Dp = 300.dp
) {
    var baseUrl by remember { mutableStateOf("") }
    var deepLink by remember { mutableStateOf("") }
    when(type) {
        PlayerType.TWITCH_PLAYER -> {
            baseUrl = "https://player.twitch.tv/"
            deepLink = "twitch://stream/dooby3d"
        }
        PlayerType.YOUTUBE_PLAYER -> {
            baseUrl = "https://www.youtube.com/"
            deepLink = "https://www.youtube.com/@dooby3d/live"
        }
    }
    val context = LocalContext.current
    AndroidView(
        modifier = modifier.fillMaxWidth().height(playerHeight),
        factory = {
            WebView(context).apply {
                setBackgroundColor(ContextCompat.getColor(context, android.R.color.black))
                webViewClient = object: WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        openExternalApp(deepLink)
                        return true
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        loadUrl("javascript:document.body.style.setProperty(\"background-color\", \"black\");")
                        super.onPageFinished(view, url)
                    }
                }
                webChromeClient = object: WebChromeClient() {
                    override fun onHideCustomView() {
                        toggleBottomBarHidden(false)
                        toggleFabHidden(false)
                        exitFullScreen()
                    }

                    override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                        println("test")
                        toggleBottomBarHidden(true)
                        toggleFabHidden(true)
                        enterFullScreen(view)
                    }
                }
                settings.useWideViewPort = true
                settings.loadWithOverviewMode = true
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.cacheMode = WebSettings.LOAD_NO_CACHE

                loadDataWithBaseURL(baseUrl, htmlString, "text/html", "UTF-8", null)
            }
        },
        update = {}
    )
}

@Composable
internal fun FullScreenPlayer(
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

internal enum class PlayerType {
    TWITCH_PLAYER,
    YOUTUBE_PLAYER
}