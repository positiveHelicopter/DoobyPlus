package com.positiveHelicopter.doobyplus.screens.socials

import android.content.pm.ActivityInfo
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
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
import com.positiveHelicopter.doobyplus.model.TwitchVideo
import com.positiveHelicopter.doobyplus.ui.NoRippleInteractionSource
import com.positiveHelicopter.doobyplus.ui.SocialsCard
import com.positiveHelicopter.doobyplus.utility.convertDurationToHHmm
import com.positiveHelicopter.doobyplus.utility.convertIsoToDDMMYYYYHHmm
import kotlinx.coroutines.launch

@Composable
internal fun SocialsScreen(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(0.dp),
    setOrientation: (Int) -> Unit = {},
    launchCustomTab: (String) -> Unit = {}
) {
    setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    SocialsScreen(
        modifier = modifier,
        launchCustomTab = launchCustomTab,
        innerPadding = innerPadding,
        logoFontFamily = FontFamily(Font(R.font.school_bell)),
        selectedTabIndex = selectedTabIndex,
        updateSelectedPrimaryTabIndex = {
            selectedTabIndex = it
        }
    )
}

@Composable
internal fun SocialsScreen(
    modifier: Modifier = Modifier,
    launchCustomTab: (String) -> Unit = {},
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
                subTabs = listOf("Videos", "Clips"),
            )
        )
    }
    val pagerState = rememberPagerState(pageCount = { tabs[selectedTabIndex].subTabs.size })
    LaunchedEffect(pagerState, selectedTabIndex) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            tabs[selectedTabIndex] = tabs[selectedTabIndex]
                .copy(selectedIndex = page)
        }
    }
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        DoobLogo(logoFontFamily = logoFontFamily)
        Column(modifier = modifier
            .padding(top = 20.dp)
            .fillMaxSize()) {
            SocialsPrimaryTabRow(
                modifier = modifier,
                tabs= tabs,
                selectedTabIndex = selectedTabIndex,
                updateSelectedPrimaryTabIndex = {
                    coroutineScope.launch {
                        updateSelectedPrimaryTabIndex(it)
                        pagerState.scrollToPage(0)
                    }
                }
            )
            SocialsSecondaryTabRow(
                modifier = modifier,
                tabs = tabs,
                selectedTabIndex = selectedTabIndex,
                scrollToPage = {
                    coroutineScope.launch {
                        pagerState.scrollToPage(it)
                    }
                }
            )
            SocialsViewPager(
                modifier = modifier.fillMaxSize(),
                pagerState = pagerState,
                launchCustomTab = launchCustomTab
            )
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
            text = "DOOBY+",
            style = TextStyle.Default.copy(
                fontSize = 44.sp,
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
                ) { append("+") }
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
            Box(
                Modifier
                    .tabIndicatorOffset(tabPositions[selectedTabIndex])
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SocialsSecondaryTabRow(
    modifier: Modifier = Modifier,
    tabs: MutableList<SocialsTab>,
    selectedTabIndex: Int,
    scrollToPage: (Int) -> Unit
) {
    SecondaryTabRow(
        modifier = modifier,
        selectedTabIndex = tabs[selectedTabIndex].selectedIndex,
        containerColor = colorResource(R.color.color_almost_black),
        indicator = {
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(
                    tabs[selectedTabIndex].selectedIndex, matchContentSize = false),
                color = colorResource(tabs[selectedTabIndex].color)
            )
        },
        divider = {}
    ) {
        tabs[selectedTabIndex].subTabs.forEachIndexed { index, s ->
            Tab(text = { Text(s, color = colorResource(R.color.color_white_faded)) },
                selected = tabs[selectedTabIndex].selectedIndex == index,
                onClick = {
                    tabs[selectedTabIndex] = tabs[selectedTabIndex]
                        .copy(selectedIndex = index)
                    scrollToPage(index)
                },
                interactionSource = NoRippleInteractionSource()
            )
        }
    }
}

@Composable
internal fun SocialsViewPager(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    launchCustomTab: (String) -> Unit = {},
) {
    HorizontalPager(
        state = pagerState,
        pageSize = PageSize.Fill,
        modifier = modifier
            .background(colorResource(R.color.color_almost_black_faded)),
        verticalAlignment = Alignment.Top
    ) { _ ->
        LazyVerticalGrid(
            modifier = modifier.padding(top = 20.dp),
            columns = GridCells.Fixed(2),
        ) {
            val videos = listOf(
                TwitchVideo(),
                TwitchVideo(title = "dooby test \uD83C\uDF83 retro halloween flash games"),
                TwitchVideo(),
                TwitchVideo(),
                TwitchVideo()
            )
            items(videos) {
                SocialsCard(
                    modifier = modifier.clickable(
                        onClick = { launchCustomTab(it.url) }
                    ),
                    title = it.title,
                    date = it.date.convertIsoToDDMMYYYYHHmm(),
                    thumbnailUrl = it.thumbnailUrl,
                    duration = it.duration.convertDurationToHHmm()
                )
            }
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