package com.positiveHelicopter.doobyplus.screens.settings

import android.content.pm.ActivityInfo
import androidx.activity.compose.BackHandler
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.positiveHelicopter.doobyplus.utility.DoobyPreview
import com.positiveHelicopter.doobyplus.R
import com.positiveHelicopter.doobyplus.model.CreditsGroup
import com.positiveHelicopter.doobyplus.model.SettingsData
import com.positiveHelicopter.doobyplus.model.SettingsGroup

@Composable
internal fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
    innerPadding: PaddingValues = PaddingValues(0.dp),
    setOrientation: (Int) -> Unit = {},
    launchCustomTab: (String, Boolean) -> Unit = { _, _ -> },
) {
    setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    val settingsState by viewModel.uiState.collectAsStateWithLifecycle()
    val settingsList = listOf(
        SettingsGroup(
            title = "General",
            settings = listOf(SETTINGS_REDIRECT_URL),
            isButton = false
        ),
        SettingsGroup(
            title = "Notifications",
            settings = listOf(
                SETTINGS_TWITCH_LIVE,
                SETTINGS_NEW_TWEET,
            ),
            isButton = false
        ),
        SettingsGroup(
            title = SETTINGS_ABOUT_DOOBY,
            settings = listOf(),
            isButton = true
        ),
        SettingsGroup(
            title = SETTINGS_CREDITS,
            settings = listOf(),
            isButton = true
        )
    )
    SettingsScreen(
        modifier = modifier,
        innerPadding = innerPadding,
        settingsState = settingsState,
        settingsList = settingsList,
        setShouldRedirectUrl = viewModel::setShouldRedirectUrl,
        setShouldSendTwitchLive = viewModel::setShouldSendTwitchLive,
        setShouldSendNewTweet = viewModel::setShouldSendNewTweet,
        launchCustomTab = launchCustomTab,
        setIsCredits = viewModel::setIsCredits
    )
}

@Composable
internal fun SettingsScreen(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(0.dp),
    settingsState: SettingsState,
    settingsList: List<SettingsGroup>,
    setShouldRedirectUrl: (Boolean) -> Unit = {},
    setShouldSendTwitchLive: (Boolean) -> Unit = {},
    setShouldSendNewTweet: (Boolean) -> Unit = {},
    launchCustomTab: (String, Boolean) -> Unit = { _, _ -> },
    setIsCredits: (Boolean) -> Unit = {}
) {
    var isCredits by remember { mutableStateOf(false) }
    val title = when(settingsState) {
        is SettingsState.Success -> {
            if (settingsState.data.isCredits) {
                isCredits = true
                BackHandler { setIsCredits(false) }
                SETTINGS_CREDITS
            }
            else {
                isCredits = false
                SETTINGS_TITLE
            }
        }
        else -> SETTINGS_TITLE
    }
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.setting_background),
            contentDescription = null,
            modifier = modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(
                    width = 2.dp,
                    color = colorResource(R.color.color_black_faded),
                    shape = RoundedCornerShape(8.dp)
                )
                .background(color = colorResource(R.color.colorPrimary))
        ) {
            Row(modifier = modifier.fillMaxWidth().wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                androidx.compose.animation.AnimatedVisibility(isCredits,
                    enter = fadeIn() + expandHorizontally(),
                    exit = fadeOut() + shrinkHorizontally()
                ) {
                    IconButton(
                        onClick = { setIsCredits(false) },
                        modifier = modifier.padding(start = 30.dp)
                    ) {
                        Icon(
                            modifier = modifier.size(30.dp),
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
                Text(text = title,
                    modifier = modifier.padding(horizontal = 30.dp, vertical = 20.dp),
                    fontSize = 35.sp,
                    color = colorResource(R.color.color_black_faded),
                    fontWeight = FontWeight.W400
                )
            }
            Box {
                androidx.compose.animation.AnimatedVisibility(
                    visible = !isCredits,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    LazyColumn(modifier = modifier.padding(horizontal = 30.dp, vertical = 20.dp)) {
                        items(settingsList.size) { index ->
                            SettingsCluster(
                                modifier = modifier,
                                settingsState = settingsState,
                                group = settingsList[index],
                                setShouldRedirectUrl = setShouldRedirectUrl,
                                setShouldSendTwitchLive = setShouldSendTwitchLive,
                                setShouldSendNewTweet = setShouldSendNewTweet,
                                launchCustomTab = launchCustomTab,
                                setIsCredits = setIsCredits
                            )
                        }
                    }
                }
                androidx.compose.animation.AnimatedVisibility(
                    visible = isCredits,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    SettingsCredits(
                        modifier = modifier,
                        settingsState = settingsState,
                        launchCustomTab = launchCustomTab
                    )
                }
            }
        }
    }
}

@Composable
internal fun SettingsCluster(
    modifier: Modifier = Modifier,
    settingsState: SettingsState,
    group: SettingsGroup,
    setShouldRedirectUrl: (Boolean) -> Unit = {},
    setShouldSendTwitchLive: (Boolean) -> Unit = {},
    setShouldSendNewTweet: (Boolean) -> Unit = {},
    launchCustomTab: (String, Boolean) -> Unit = { _, _ -> },
    setIsCredits: (Boolean) -> Unit = {}
) {
    val shouldRedirectUrl = when (settingsState) {
        is SettingsState.Success -> settingsState.data.shouldRedirectUrl
        is SettingsState.Loading -> true
    }
    if (group.isButton) {
        val aboutLink = stringResource(R.string.about_dooby_link)
        var bottomPadding by remember { mutableStateOf(20.dp) }
        val onClick: () -> Unit = when (group.title) {
            SETTINGS_ABOUT_DOOBY -> {
                bottomPadding = 0.dp
                { launchCustomTab(aboutLink, shouldRedirectUrl) }
            }
            else -> {
                bottomPadding = 20.dp
                { setIsCredits(true) }
            }
        }
        TextButton(onClick = onClick,
            modifier = modifier.padding(bottom = bottomPadding),
            colors = ButtonDefaults.textButtonColors().copy(
                contentColor = colorResource(R.color.color_dark_blue)
            )) {
            Text(text = group.title, fontSize = 14.sp)
        }
        return
    }
    Text(text = group.title,
        fontSize = 21.sp,
        color = colorResource(R.color.backgroundColor),
        fontWeight = FontWeight.W400
    )
    Column(modifier = modifier
        .padding(top = 10.dp, bottom = 20.dp)
        .fillMaxWidth()
        .wrapContentHeight()
        .clip(RoundedCornerShape(8.dp))
        .background(color = colorResource(android.R.color.transparent))
        .border(
            width = 1.dp,
            color = colorResource(R.color.color_black_faded),
            shape = RoundedCornerShape(8.dp)
        )
    ) {
        group.settings.forEach {
            SettingsRow(
                modifier = modifier,
                settingsState = settingsState,
                text = it,
                setShouldRedirectUrl = setShouldRedirectUrl,
                setShouldSendTwitchLive = setShouldSendTwitchLive,
                setShouldSendNewTweet = setShouldSendNewTweet
            )
        }
    }
}

@Composable
internal fun SettingsRow(
    modifier: Modifier = Modifier,
    settingsState: SettingsState,
    text: String,
    setShouldRedirectUrl: (Boolean) -> Unit = {},
    setShouldSendTwitchLive: (Boolean) -> Unit = {},
    setShouldSendNewTweet: (Boolean) -> Unit = {}
) {
    var checked by remember { mutableStateOf(true) }
    var onCheckedChange: (Boolean) -> Unit = {}
    when (settingsState) {
        is SettingsState.Success -> {
            when (text) {
                SETTINGS_REDIRECT_URL -> {
                    checked = settingsState.data.shouldRedirectUrl
                    onCheckedChange = { setShouldRedirectUrl(it) }
                }
                SETTINGS_TWITCH_LIVE -> {
                    checked = settingsState.data.shouldSendTwitchLive
                    onCheckedChange = { setShouldSendTwitchLive(it) }
                }
                SETTINGS_NEW_TWEET -> {
                    checked = settingsState.data.shouldSendNewTweet
                    onCheckedChange = { setShouldSendNewTweet(it) }
                }
            }
        }
        else -> {}
    }
    Row(modifier = modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = text,
            modifier = modifier
                .fillMaxWidth(.8f),
            fontSize = 16.sp,
            color = colorResource(R.color.color_black_faded),
            fontWeight = FontWeight.W400
        )
        Switch(
            modifier = modifier,
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors().copy(
                checkedThumbColor = colorResource(R.color.color_white_faded),
                checkedTrackColor = colorResource(R.color.color_almost_black_faded),
                checkedBorderColor = colorResource(R.color.color_almost_black_faded)
            )
        )
    }
}

@Composable
internal fun SettingsCredits(
    modifier: Modifier = Modifier,
    settingsState: SettingsState,
    launchCustomTab: (String, Boolean) -> Unit = { _, _ -> }
) {
    val shouldRedirectUrl = when (settingsState) {
        is SettingsState.Success -> settingsState.data.shouldRedirectUrl
        is SettingsState.Loading -> true
    }
    val credits = listOf(
        CreditsGroup(
            name = stringResource(R.string.credit_dooby),
            handle = stringResource(R.string.credit_dooby_twitter),
            description = stringResource(R.string.credit_dooby_description)
        ),
        CreditsGroup(
            name = stringResource(R.string.credit_splash_screen),
            handle = stringResource(R.string.credit_splash_screen_twitter),
            description = stringResource(R.string.credit_splash_screen_description)
        ),
    )
    LazyColumn(modifier = modifier.padding(horizontal = 30.dp, vertical = 20.dp)) {
        items(credits.size) { index ->
            Text(
                text = buildAnnotatedString {
                    append("${credits[index].description} - ")
                    append("${credits[index].name} ")
                    val link = "https://x.com/${
                        credits[index].handle.replace("@", "")
                    }"
                    withLink(
                        LinkAnnotation.Url(
                            url = link,
                            styles = TextLinkStyles(
                                style = SpanStyle(
                                    color = colorResource(R.color.color_dark_blue)
                                )
                            )
                        ) {
                            launchCustomTab(link, shouldRedirectUrl)
                        }
                    ) {
                        append(credits[index].handle)
                    }
                },
                modifier = modifier.padding(vertical = 5.dp),
                fontSize = 20.sp,
                color = colorResource(R.color.color_black_faded),
            )
        }
    }
}

@DoobyPreview
@Composable
internal fun SettingsScreenPreview() {
    SettingsScreen(
        settingsState = SettingsState.Success(
            SettingsData(
                shouldRedirectUrl = true,
                shouldSendTwitchLive = true,
                shouldSendNewTweet = false,
                isCredits = true
            )
        ),
        settingsList = listOf(
            SettingsGroup(
                title = "General",
                settings = listOf(SETTINGS_REDIRECT_URL),
                isButton = false
            ),
            SettingsGroup(
                title = "Notifications",
                settings = listOf(
                    SETTINGS_TWITCH_LIVE,
                    SETTINGS_NEW_TWEET
                ),
                isButton = false
            ),
            SettingsGroup(
                title = SETTINGS_ABOUT_DOOBY,
                settings = listOf(),
                isButton = true
            ),
            SettingsGroup(
                title = SETTINGS_CREDITS,
                settings = listOf(),
                isButton = true
            )
        )
    )
}

internal const val SETTINGS_REDIRECT_URL = "Redirect to External Apps"
internal const val SETTINGS_TWITCH_LIVE = "Twitch Goes Live"
internal const val SETTINGS_NEW_TWEET = "New Tweet"
internal const val SETTINGS_ABOUT_DOOBY = "About Dooby3D"
internal const val SETTINGS_CREDITS = "Credits"
internal const val SETTINGS_TITLE = "Settings"