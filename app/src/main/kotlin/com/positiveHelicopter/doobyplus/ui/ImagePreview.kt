package com.positiveHelicopter.doobyplus.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import com.positiveHelicopter.doobyplus.R

@Composable
internal fun ImagePreview(
    modifier: Modifier = Modifier,
    url: String = "",
    setPreviewImage: (Boolean, String) -> Unit = { _, _ -> }
) {
    BackHandler { setPreviewImage(false, "") }
    Box(modifier = modifier
        .fillMaxSize()
        .zIndex(1f)
        .clickable(
            interactionSource = null,
            indication = null,
            onClick = {}
        )
        .background(colorResource(R.color.color_black_faded))
    ) {
        AsyncImage(
            model = url,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit,
            error = painterResource(R.drawable.jerboa_erm)
        )
    }
}