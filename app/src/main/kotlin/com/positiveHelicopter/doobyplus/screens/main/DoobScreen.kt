package com.positiveHelicopter.doobyplus.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.positiveHelicopter.doobyplus.R

@Composable
internal fun DoobScreen(
    modifier: Modifier = Modifier
) {
    val background = painterResource(R.drawable.doob_background)
    Box(modifier.fillMaxSize()
        .background(colorResource(id = R.color.backgroundColor))
        .paint(painter = background, contentScale = ContentScale.FillWidth)
    ) {

    }
}

@Preview(widthDp = 720, heightDp = 1280)
@Composable
internal fun DoobScreenPreview() {
    DoobScreen()
}