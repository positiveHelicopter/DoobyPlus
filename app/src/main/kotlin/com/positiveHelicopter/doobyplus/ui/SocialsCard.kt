package com.positiveHelicopter.doobyplus.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.positiveHelicopter.doobyplus.utility.DoobyPreview
import com.positiveHelicopter.doobyplus.R

@Composable
internal fun SocialsCard(
    modifier: Modifier = Modifier,
    title: String = "",
    date: String = "",
    thumbnailUrl: String = "",
    duration: String = "",
) {
    var contentScale by remember { mutableStateOf(ContentScale.Fit) }
    OutlinedCard(
        modifier = Modifier.wrapContentHeight().fillMaxWidth()
            .padding(bottom = 20.dp, start = 10.dp, end = 10.dp),
        border = BorderStroke(width = 1.dp, color = colorResource(R.color.black)),
        colors = CardDefaults.outlinedCardColors().copy(
            containerColor = colorResource(R.color.color_almost_black)
        )
    ) {
        Column(modifier = modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp, max = 120.dp)) {
                AsyncImage(
                    model = thumbnailUrl,
                    contentDescription = title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = contentScale,
                    onSuccess = { contentScale = ContentScale.FillBounds },
                    placeholder = painterResource(R.drawable.jerboa_erm),
                    error = painterResource(R.drawable.jerboa_erm)
                )
                Text(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(5.dp)
                        .background(colorResource(R.color.color_black_faded))
                        .padding(3.dp),
                    text = duration,
                    color = colorResource(R.color.color_white_faded),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp, top = 10.dp),
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = colorResource(R.color.color_white_faded),
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 5.dp, bottom = 10.dp, start = 15.dp, end = 15.dp),
                text = date,
                color = colorResource(R.color.color_grey_faded),
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
@DoobyPreview
internal fun SocialsCardPreview() {
    SocialsCard(
        title = "dooby test \uD83C\uDF83 retro halloween flash games \uD83C\uDF83 roblox maybe?" +
                " \uD83C\uDF83 happy halloweeeeeeen",
        date = "31/10/2024 05:07",
        duration = "30:57"
    )
}