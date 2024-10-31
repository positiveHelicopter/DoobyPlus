package com.positiveHelicopter.doobyplus.screens.socials

import android.content.pm.ActivityInfo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.positiveHelicopter.doobyplus.utility.DoobyPreview
import com.positiveHelicopter.doobyplus.R

@Composable
internal fun SocialsScreen(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(0.dp),
    setOrientation: (Int) -> Unit = {}
) {
    setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    Box(modifier = modifier.fillMaxSize().padding(innerPadding)
        .padding(top = 20.dp),
        contentAlignment = Alignment.TopCenter) {
        DoobLogo()
    }
}

@Composable
internal fun DoobLogo(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = "DOOBY3D",
        style = TextStyle.Default.copy(
            fontSize = 45.sp,
            drawStyle = Stroke(
                miter = 10f,
                width = 7f,
                join = StrokeJoin.Round
            ),
            color = colorResource(R.color.black)
        ),
        fontFamily = FontFamily(Font(R.font.school_bell))
    )
    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(color = colorResource(R.color.colorPrimary))
            ) { append("D") }
            withStyle(
                style = SpanStyle(color = colorResource(R.color.color_light_blue))
            ) { append("OO") }
            withStyle(
                style = SpanStyle(color = colorResource(R.color.color_light_red))
            ) { append("B") }
            withStyle(
                style = SpanStyle(color = colorResource(R.color.colorPrimary))
            ) { append("Y") }
            withStyle(
                style = SpanStyle(color = colorResource(R.color.color_light_blue))
            ) { append("3") }
            withStyle(
                style = SpanStyle(color = colorResource(R.color.color_light_red))
            ) { append("D") }
        },
        fontSize = 45.sp,
        fontFamily = FontFamily(Font(R.font.school_bell))
    )
}

@DoobyPreview
@Composable
internal fun SocialsScreenPreview() {
    SocialsScreen()
}