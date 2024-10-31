package com.positiveHelicopter.doobyplus.screens.socials

import android.content.pm.ActivityInfo
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.positiveHelicopter.doobyplus.utility.DoobyPreview
import com.positiveHelicopter.doobyplus.R
import com.positiveHelicopter.doobyplus.model.SocialsTab
import com.positiveHelicopter.doobyplus.ui.NoRippleInteractionSource

@Composable
internal fun SocialsScreen(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(0.dp),
    setOrientation: (Int) -> Unit = {},
) {
    setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    SocialsScreen(
        modifier = modifier,
        innerPadding = innerPadding,
        logoFontFamily = FontFamily(Font(R.font.school_bell)),
        selectedTabIndex = selectedTabIndex,
        updateSelectedPrimaryTabIndex = {
            selectedTabIndex = it
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SocialsScreen(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(0.dp),
    logoFontFamily: FontFamily?,
    selectedTabIndex: Int,
    updateSelectedPrimaryTabIndex: (Int) -> Unit
) {
    val tabs = remember {
        mutableStateListOf(
            SocialsTab(
                title = "Youtube",
                icon = R.drawable.youtube_logo,
                monoIcon = R.drawable.youtube_logo_mono,
                color = R.color.youtube_color,
                subTabs = listOf("Videos", "Shorts", "Live")
            ),
            SocialsTab(
                title = "Twitch",
                icon = R.drawable.twitch_logo,
                monoIcon = R.drawable.twitch_logo_mono,
                color = R.color.twitch_color,
                subTabs = listOf("Videos", "Clips")
            )
        )
    }
    Column(modifier = modifier.fillMaxSize().padding(innerPadding)
        .padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        DoobLogo(logoFontFamily = logoFontFamily)
        Column(modifier = modifier.padding(vertical = 20.dp).fillMaxSize()) {
            SocialsPrimaryTabRow(
                modifier = modifier,
                tabs= tabs,
                selectedTabIndex = selectedTabIndex,
                updateSelectedPrimaryTabIndex = updateSelectedPrimaryTabIndex
            )
            SecondaryTabRow(
                selectedTabIndex = tabs[selectedTabIndex].selectedIndex,
                containerColor = colorResource(R.color.color_almost_black),
                indicator = {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabs[selectedTabIndex].selectedIndex, matchContentSize = false),
                        color = colorResource(tabs[selectedTabIndex].color)
                    )
                },
                divider = {}
            ) {
                tabs[selectedTabIndex].subTabs.forEachIndexed { index, s ->
                    Tab(text = { Text(s, color = colorResource(R.color.white)) },
                        selected = tabs[selectedTabIndex].selectedIndex == index,
                        onClick = {
                            tabs[selectedTabIndex] = tabs[selectedTabIndex]
                                .copy(selectedIndex = index)
                        },
                        interactionSource = NoRippleInteractionSource()
                    )
                }
            }
        }
    }
}

@Composable
internal fun DoobLogo(
    modifier: Modifier = Modifier,
    logoFontFamily: FontFamily? = null
) {
    Box(modifier = modifier) {
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
            fontFamily = logoFontFamily
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
            fontFamily = logoFontFamily
        )
    }
}

@Composable
internal fun SocialsPrimaryTabRow(
    modifier: Modifier = Modifier,
    tabs: List<SocialsTab>,
    selectedTabIndex: Int,
    updateSelectedPrimaryTabIndex: (Int) -> Unit
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = colorResource(android.R.color.transparent),
        divider = {},
        indicator = { tabPositions ->
            Box(Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex])
                .fillMaxHeight()
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .zIndex(-1f)
                .background(colorResource(tabs[selectedTabIndex].color))) {}
        }
    ) {
        tabs.forEachIndexed { index, data ->
            val selected = selectedTabIndex == index
            Tab(
                modifier = modifier.clip(
                    RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                ),
                icon = { Image(
                    modifier = modifier.size(30.dp),
                    painter = painterResource(
                        if (selected) data.monoIcon else data.icon
                    ),
                    contentDescription = ""
                ) },
                selected = selected,
                onClick = { updateSelectedPrimaryTabIndex(index) },
                interactionSource = NoRippleInteractionSource()
            )
        }
    }
}

@DoobyPreview
@Composable
internal fun SocialsScreenPreview() {
    SocialsScreen(
        logoFontFamily = null,
        selectedTabIndex = 0,
        updateSelectedPrimaryTabIndex = {}
    )
}