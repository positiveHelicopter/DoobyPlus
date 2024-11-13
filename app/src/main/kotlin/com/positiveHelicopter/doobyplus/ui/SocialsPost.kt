package com.positiveHelicopter.doobyplus.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.positiveHelicopter.doobyplus.R
import com.positiveHelicopter.doobyplus.utility.DoobyPreview
import com.positiveHelicopter.doobyplus.utility.convertTweetDateToDDMMMYYYYHHmm
import com.positiveHelicopter.doobyplus.utility.findHttpsUrl

@Composable
internal fun SocialsPost(
    modifier: Modifier = Modifier,
    id: String,
    text: String,
    date: String,
    link: String,
    previewLink: String,
    shouldShowImage: Boolean = true,
    launchCustomTab: (String) -> Unit = {},
    updatePreviewLink: (String, String, String) -> Unit = { _, _, _ -> },
    setPreviewImage: (Boolean, String) -> Unit = { _, _ -> }
) {
    if (previewLink.isEmpty()) updatePreviewLink(id, text, link)
    Row {
        if(shouldShowImage) {
            Image(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .size(50.dp)
                    .clip(CircleShape),
                painter = painterResource(R.drawable.dooby_face),
                contentDescription = "Dooby"
            )
        } else {
            Spacer(modifier.width(60.dp))
        }
        Surface(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(bottom = 20.dp, start = 10.dp, end = 10.dp),
            color = colorResource(R.color.color_almost_black),
            shape = BubbleShape(shouldShowImage),
            border = BorderStroke(width = 1.dp, color = colorResource(R.color.black))
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .wrapContentHeight(),
            ) {
                Text(
                    text = buildAnnotatedString {
                        val httpsParts = text.findHttpsUrl()
                        if (httpsParts.isEmpty()) {
                            append(text)
                            return@buildAnnotatedString
                        }
                        append(text.substring(0, httpsParts[0].startIndex))
                        httpsParts.forEachIndexed { index, part ->
                            withLink(
                                LinkAnnotation.Url(
                                    url = part.url,
                                    styles = TextLinkStyles(
                                        style = SpanStyle(
                                            color = colorResource(R.color.color_light_blue)
                                        )
                                    )
                                ) {
                                    launchCustomTab(part.url)
                                }
                            ) {
                                append(part.url)
                            }
                            append(text.substring(
                                part.endIndex,
                                if (index + 1 < httpsParts.size)
                                    httpsParts[index + 1].startIndex
                                else text.length))
                        }
                    },
                    modifier = Modifier.padding(
                        start = 30.dp, end = 15.dp, top = 10.dp, bottom = 10.dp),
                    color = colorResource(R.color.color_white_faded),
                    fontSize = 16.sp
                )
                Text(
                    modifier = Modifier.fillMaxWidth()
                        .padding(bottom = 10.dp, start = 10.dp, end = 15.dp),
                    text = date.convertTweetDateToDDMMMYYYYHHmm(),
                    color = colorResource(R.color.color_grey_faded),
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.End
                )
                SocialsPostImage(
                    text = text,
                    link = link,
                    previewLink = previewLink,
                    launchCustomTab = launchCustomTab,
                    setPreviewImage = setPreviewImage
                )
            }
        }
    }
}

@Composable
internal fun SocialsPostImage(
    text: String,
    link: String,
    previewLink: String,
    launchCustomTab: (String) -> Unit = {},
    setPreviewImage: (Boolean, String) -> Unit = { _, _ -> }
) {
    Column(modifier = Modifier.fillMaxWidth()
        .wrapContentHeight()
        .background(colorResource(R.color.color_black_faded))) {
        Text(
            modifier = Modifier.padding(
                start = 30.dp, end = 15.dp, top = 10.dp, bottom = 10.dp),
            text = buildAnnotatedString {
                withLink(
                    LinkAnnotation.Url(
                        url = link,
                        styles = TextLinkStyles(
                            style = SpanStyle(
                                color = colorResource(R.color.color_white_faded)
                            )
                        )
                    ) {
                        launchCustomTab(link)
                    }
                ) {
                    append(link)
                }
            },
            fontSize = 14.sp
        )
        if (previewLink.isEmpty() || previewLink == "null") return
        var tag by remember { mutableStateOf("") }
        if (previewLink.contains("ext_tw_video", ignoreCase = true)) {
            tag = "Video Preview"
        }
        if (previewLink.contains("tweet_video", ignoreCase = true)) {
            tag = "GIF Preview"
        }
        Box {
            AsyncImage(
                model = previewLink,
                contentDescription = text,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp, max = 300.dp)
                    .padding(start = 12.dp)
                    .clickable { setPreviewImage(true, previewLink) },
                contentScale = ContentScale.Fit,
                placeholder = painterResource(R.drawable.jerboa_erm),
                error = painterResource(R.drawable.jerboa_erm)
            )
            if (tag.isEmpty()) return@Box
            Text(text = tag,
                modifier = Modifier.align(Alignment.BottomStart)
                    .padding(start = 20.dp, bottom = 10.dp)
                    .background(color = colorResource(R.color.color_almost_black_high_faded))
                    .padding(5.dp),
                color = colorResource(R.color.color_white_faded),
                fontSize = 12.sp
            )
        }
    }
}

@DoobyPreview
@Composable
internal fun SocialsPostPreview() {
    SocialsPost(
        id = "",
        text = "This is a post This is a post This is a post This is a post This is a post This is a post",
        date = " November 03, 2024 at 01:03PM",
        link = "https://x.com/dooby3D/status/1853188180381716578",
        previewLink = ""
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