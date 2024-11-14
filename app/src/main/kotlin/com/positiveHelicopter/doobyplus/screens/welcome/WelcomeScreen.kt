package com.positiveHelicopter.doobyplus.screens.welcome

import android.graphics.drawable.Animatable
import android.widget.ImageView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
    Box(modifier = modifier.fillMaxSize()
        .background(colorResource(R.color.color_welcome_green))) {
        Text(
            modifier = modifier.fillMaxWidth().padding(top = 50.dp + topInset),
            text = stringResource(R.string.welcome_title),
            fontSize = 35.sp,
            color = colorResource(R.color.color_black_faded),
            fontWeight = FontWeight.W400,
            textAlign = TextAlign.Center
        )
        AndroidView(
            modifier = modifier.align(Alignment.Center),
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
        Column(modifier = modifier.align(Alignment.BottomCenter)
            .fillMaxWidth()
            .padding(bottom = 30.dp + botInset),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = stringResource(R.string.welcome_message),
                modifier = modifier.fillMaxWidth().padding(20.dp),
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )
            Button(
                modifier = modifier.padding(10.dp),
                onClick = {
                    askNotificationPermission { requestPermission ->
                        requestPermission()
                    }
                },
                contentPadding = PaddingValues(horizontal = 50.dp, vertical = 15.dp),
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = colorResource(R.color.color_dark_green)
                )
            ) {
                Text(text = stringResource(R.string.welcome_allow_notifications),
                    fontSize = 16.sp)
            }
            TextButton(onClick = {
                setIsFirstTimeNotification(false)
            }) {
                Text(text = stringResource(R.string.welcome_skip),
                    fontSize = 16.sp,
                    color = colorResource(R.color.color_dark_green))
            }
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