package com.positiveHelicopter.doobyplus.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.positiveHelicopter.doobyplus.R
import com.positiveHelicopter.doobyplus.utility.DoobyPreview

@Composable
internal fun SocialsPost(
    modifier: Modifier = Modifier,
    text: String,
    url: String = "",
    shouldShowImage: Boolean = true
) {
    Row {
        if(shouldShowImage) {
            Image(
                modifier = Modifier.padding(start = 10.dp).size(50.dp).clip(CircleShape),
                painter = painterResource(R.drawable.dooby_face),
                contentDescription = "Dooby"
            )
        } else {
            Spacer(modifier.width(60.dp))
        }
        Surface(
            modifier = Modifier.wrapContentHeight().fillMaxWidth()
                .padding(bottom = 20.dp, start = 10.dp, end = 10.dp),
            color = colorResource(R.color.color_almost_black),
            shape = BubbleShape(shouldShowImage),
            border = BorderStroke(width = 1.dp, color = colorResource(R.color.black))
        ) {
            Column(
                modifier = modifier.fillMaxSize().wrapContentHeight(),
            ) {
                Text(
                    text = text,
                    modifier = Modifier.padding(horizontal = 30.dp, vertical = 10.dp),
                    color = colorResource(R.color.color_white_faded),
                    fontSize = 16.sp
                )
                AsyncImage(
                    model = url,
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
            }
        }
    }
}

@DoobyPreview
@Composable
internal fun SocialsPostPreview() {
    SocialsPost(
        text = "This is a post This is a post This is a post This is a post This is a post This is a post"
    )
}

class BubbleShape(
    private val shouldShowImage: Boolean = true
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            if (shouldShowImage) {
                moveTo(0f, 0f)
            } else {
                moveTo(60f, 0f)
            }
            lineTo(size.width - 30f, 0f)
            arcTo(rect = Rect(size.width - 30f, 0f, size.width, 30f),
                startAngleDegrees = -90f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false)
            lineTo(size.width, size.height - 30f)
            arcTo(rect = Rect(size.width - 30f, size.height - 30f, size.width, size.height),
                startAngleDegrees = 0f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false)
            lineTo(60f, size.height)
            arcTo(rect = Rect(30f, size.height - 30f, 60f, size.height),
                startAngleDegrees = 90f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false)
            lineTo(30f, 30f)
            if (!shouldShowImage) arcTo(rect = Rect(30f, 0f, 60f, 30f),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false)
            close()
        }
        return Outline.Generic(path)
    }
}