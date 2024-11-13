package com.positiveHelicopter.doobyplus.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import com.positiveHelicopter.doobyplus.R
import com.positiveHelicopter.doobyplus.utility.DoobyPreview

@Composable
internal fun ImagePreview(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(0.dp),
    url: String = "",
    setPreviewImage: (Boolean, String) -> Unit = { _, _ -> }
) {
    var isBackVisible by remember { mutableStateOf(true) }
    var zoomScale by remember { mutableFloatStateOf(MIN_ZOOM) }
    var zoomOffset by remember { mutableStateOf(Offset.Zero) }
    BackHandler { setPreviewImage(false, "") }
    Box(modifier = modifier
        .fillMaxSize()
        .zIndex(1f)
        .pointerInput(Unit) {
            detectTapGestures(
                onTap = {
                    isBackVisible = !isBackVisible
                },
                onDoubleTap = { offset ->
                    isBackVisible = false
                    zoomOffset = if (zoomScale < MED_ZOOM)
                        calculateOffset(offset, size)
                    else Offset.Zero
                    zoomScale = if (zoomScale < MED_ZOOM)
                        MED_ZOOM
                    else MIN_ZOOM
                }
            )
        }
        .pointerInput(Unit) {
            detectDragGestures { _, dragAmount ->
                zoomOffset = boundTranslation(
                    zoomOffset.x + dragAmount.x,
                    zoomOffset.y + dragAmount.y,
                    size,
                    zoomScale
                )
            }
        }
        .pointerInput(Unit) {
            detectTransformGestures(
                onGesture = { _, gesturePan, gestureZoom, _ ->
                    zoomScale = (zoomScale * gestureZoom).coerceIn(MIN_ZOOM, MAX_ZOOM)
                    val newOffset = zoomOffset + gesturePan.times(zoomScale)
                    zoomOffset = boundTranslation(newOffset.x, newOffset.y, size, zoomScale)
                }
            )
        }
        .background(colorResource(R.color.color_black_faded))
    ) {
        AsyncImage(
            model = url,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
                .graphicsLayer {
                    scaleX = zoomScale
                    scaleY = zoomScale
                    translationX = zoomOffset.x
                    translationY = zoomOffset.y
                },
            contentScale = ContentScale.Fit,
            error = painterResource(R.drawable.jerboa_erm)
        )
        AnimatedVisibility(isBackVisible,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Box(modifier = modifier
                .fillMaxWidth()
                .background(colorResource(R.color.black)),
                contentAlignment = Alignment.CenterStart
            ) {
                IconButton(
                    modifier = modifier.padding(top = innerPadding.calculateTopPadding())
                        .padding(10.dp),
                    onClick = { setPreviewImage(false, "") }
                ) {
                    Icon(
                        modifier = modifier.size(30.dp),
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = colorResource(R.color.color_white_faded)
                    )
                }
            }
        }
    }
}

private fun calculateOffset(tapOffset: Offset, size: IntSize): Offset {
    val offsetX = (-(tapOffset.x - (size.width / 2f)) * 2f)
        .coerceIn(-size.width / 2f, size.width / 2f)
    return Offset(offsetX, 0f)
}

private fun boundTranslation(
    newOffsetX: Float,
    newOffsetY: Float,
    size: IntSize,
    zoom: Float
): Offset {
    val maxX = (size.width * (zoom - 1) / 2f)
    val maxY = (size.height * (zoom - 1) / 2f)

    return Offset(
        newOffsetX.coerceIn(-maxX, maxX),
        newOffsetY.coerceIn(-maxY, maxY))
}

@DoobyPreview
@Composable
internal fun ImagePreviewPreview() {
    ImagePreview()
}

internal const val MIN_ZOOM = 1f
internal const val MED_ZOOM = 2f
internal const val MAX_ZOOM = 3f