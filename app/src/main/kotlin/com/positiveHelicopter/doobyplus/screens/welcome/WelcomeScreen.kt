package com.positiveHelicopter.doobyplus.screens.welcome

import android.graphics.drawable.Animatable
import android.widget.ImageView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.positiveHelicopter.doobyplus.utility.DoobyPreview
import com.positiveHelicopter.doobyplus.R

@Composable
internal fun WelcomeScreen(
    modifier: Modifier = Modifier,
    askNotificationPermission: ((() -> Unit) -> Unit) -> Unit = {},
    setIsFirstTimeNotification: (Boolean) -> Unit = {}
) {
    val topInset = with(LocalDensity.current) {
        WindowInsets.Companion.systemBars.getTop(LocalDensity.current).toDp()
    }
    val botInset = with(LocalDensity.current) {
        WindowInsets.Companion.systemBars.getBottom(LocalDensity.current).toDp()
    }
    Column(modifier = modifier.fillMaxSize()
        .background(colorResource(R.color.color_welcome_green))
        .padding(top = 50.dp + topInset, bottom = botInset)) {
        Text(
            modifier = modifier.fillMaxWidth()
                .weight(2f),
            text = stringResource(R.string.welcome_title),
            fontSize = 35.sp,
            color = colorResource(R.color.color_black_faded),
            fontWeight = FontWeight.W400,
            textAlign = TextAlign.Center
        )
        AndroidView(
            modifier = modifier.align(Alignment.CenterHorizontally)
                .weight(3f),
            factory = {
                val view = ImageView(it)
                view.setBackgroundResource(R.drawable.welcome)
                val background = view.background
                if (background is Animatable) {
                    background.start()
                }
                view
            },
            update = {}
        )
        Text(text = stringResource(R.string.welcome_message),
            modifier = modifier.fillMaxWidth().padding(horizontal = 20.dp)
                .weight(1.5f),
            textAlign = TextAlign.Center,
            fontSize = 16.sp
        )
        var paddingValues by remember { mutableStateOf(PaddingValues(
            horizontal = 50.dp, vertical = 15.dp)) }
        Button(
            modifier = modifier.padding(10.dp)
                .align(Alignment.CenterHorizontally)
                .weight(1f, fill = false),
            onClick = {
                askNotificationPermission { requestPermission ->
                    requestPermission()
                }
            },
            contentPadding = paddingValues,
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = colorResource(R.color.color_dark_green)
            )
        ) {
            Text(text = stringResource(R.string.welcome_allow_notifications),
                fontSize = 16.sp,
                onTextLayout = { result ->
                    if (result.didOverflowHeight) {
                        paddingValues = ButtonDefaults.ContentPadding
                    }
                })
        }
        TextButton(
            modifier = modifier.align(Alignment.CenterHorizontally)
                .weight(1f, fill = false),
            onClick = { setIsFirstTimeNotification(false) }
        ) {
            Text(text = stringResource(R.string.welcome_skip),
                fontSize = 16.sp,
                color = colorResource(R.color.color_dark_green))
        }
    }
}

@DoobyPreview
@Composable
internal fun WelcomeScreenPreview() {
    WelcomeScreen(
        askNotificationPermission = {}
    )
}