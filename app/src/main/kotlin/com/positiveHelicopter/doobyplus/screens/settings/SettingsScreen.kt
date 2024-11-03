package com.positiveHelicopter.doobyplus.screens.settings

import android.content.pm.ActivityInfo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.positiveHelicopter.doobyplus.utility.DoobyPreview
import com.positiveHelicopter.doobyplus.R

@Composable
internal fun SettingsScreen(
    modifier: Modifier = Modifier,
    setOrientation: (Int) -> Unit = {}
) {
    setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    Box(modifier.fillMaxSize().paint(
        painterResource(R.drawable.setting_background),
        contentScale = ContentScale.FillHeight
    )) {  }
    //options
    //Twitch - getNotificationWhenLive, redirectUrlToTwitchApp
    //Youtube - getNotificationWhenLive, redirectUrlToYoutubeApp
    //watch? or chat? - watch with youtube or twitch player
    //credits
}

@DoobyPreview
@Composable
internal fun SettingsScreenPreview() {
    SettingsScreen()
}