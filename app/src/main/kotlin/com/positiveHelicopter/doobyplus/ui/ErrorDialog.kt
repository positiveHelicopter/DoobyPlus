package com.positiveHelicopter.doobyplus.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.positiveHelicopter.doobyplus.utility.DoobyPreview
import com.positiveHelicopter.doobyplus.R

@Composable
internal fun ErrorDialog(
    modifier: Modifier = Modifier,
    title: String = "",
    text: String = "",
    onDismissRequest: () -> Unit = {}
) {
    val iconSize = 100.dp
    Dialog(onDismissRequest = onDismissRequest) {
        Box(modifier = modifier.fillMaxWidth().wrapContentHeight()) {
            Image(
                modifier = modifier.size(iconSize).clip(shape = RoundedCornerShape(8.dp)),
                painter = painterResource(R.drawable.dooby_cope),
                contentDescription = null
            )
            Image(
                modifier = modifier.size(iconSize).clip(shape = RoundedCornerShape(8.dp))
                    .align(Alignment.TopEnd)
                    .scale(scaleX = -1f, scaleY = 1f),
                painter = painterResource(R.drawable.dooby_error),
                contentDescription = null
            )
            Card(modifier = modifier.padding(top = iconSize).fillMaxWidth().wrapContentHeight(),
                border = BorderStroke(width = 2.dp, color = colorResource(R.color.color_black_faded)),
                colors = CardDefaults.cardColors().copy(
                    containerColor = colorResource(R.color.colorPrimary),
                )
            ) {
                Column(modifier = modifier.fillMaxWidth().wrapContentHeight().padding(
                    horizontal = 30.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(modifier = modifier,
                        text = title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center)
                    Text(modifier = modifier.padding(top = 16.dp),
                        text = text,
                        fontSize = 16.sp)
                    Button(
                        modifier = modifier.padding(top = 24.dp),
                        onClick = onDismissRequest,
                        colors = ButtonDefaults.buttonColors().copy(
                            containerColor = colorResource(R.color.color_purple)
                        )
                    ) {
                        Text(text = "Continue", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

@DoobyPreview
@Composable
internal fun ErrorDialogPreview() {
    ErrorDialog(
        title = "Error Title Error Title Error Title Error Title Error Title Error Title Error Title Error Title Error Title Error Title Error Title Error Title",
        text = "Error Text Error Text Error Text Error Text Error Text Error Text Error Text Error Text Error Text Error Text Error Text Error Text",
        onDismissRequest = {}
    )
}