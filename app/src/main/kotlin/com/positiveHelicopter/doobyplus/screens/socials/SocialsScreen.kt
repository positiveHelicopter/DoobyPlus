package com.positiveHelicopter.doobyplus.screens.socials

import android.content.pm.ActivityInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.positiveHelicopter.doobyplus.utility.DoobyPreview

@Composable
internal fun SocialsScreen(
    modifier: Modifier = Modifier,
    setOrientation: (Int) -> Unit = {}
) {
    setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
}

@DoobyPreview
@Composable
internal fun SocialsScreenPreview() {
    SocialsScreen()
}