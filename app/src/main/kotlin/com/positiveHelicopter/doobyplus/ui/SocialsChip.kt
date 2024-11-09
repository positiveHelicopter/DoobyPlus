package com.positiveHelicopter.doobyplus.ui

import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.positiveHelicopter.doobyplus.utility.DoobyPreview
import com.positiveHelicopter.doobyplus.R

@Composable
internal fun SocialsChip(
    modifier: Modifier = Modifier,
    selected: Boolean,
    text: String = "",
    onClick: () -> Unit = {}
) {
    FilterChip(
        modifier = modifier,
        selected = selected,
        label = {
            Text(
                text = text,
                color = colorResource(R.color.color_white_faded),
                fontSize = 14.sp
            )
        },
        onClick = onClick,
        colors = FilterChipDefaults.filterChipColors().copy(
            containerColor = colorResource(android.R.color.transparent),
            selectedContainerColor = colorResource(R.color.twitch_color)
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            selectedBorderWidth = 0.dp,
            borderWidth = 1.dp,
            selectedBorderColor = colorResource(android.R.color.transparent)
        ),
    )
}

@DoobyPreview
@Composable
internal fun SocialsChipPreview() {
    SocialsChip(
        selected = true,
        text = "Testing"
    )
}