package com.positiveHelicopter.doobyplus.screens.settings

import android.content.pm.ActivityInfo
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.positiveHelicopter.doobyplus.utility.DoobyPreview
import com.positiveHelicopter.doobyplus.R
import com.positiveHelicopter.doobyplus.model.SettingsData
import com.positiveHelicopter.doobyplus.model.SettingsGroup

@Composable
internal fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
    innerPadding: PaddingValues = PaddingValues(0.dp),
    setOrientation: (Int) -> Unit = {}
) {
    setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    val settingsState by viewModel.uiState.collectAsStateWithLifecycle()
    val settingsList = listOf(
        SettingsGroup(
            title = "General",
            settings = listOf(SETTINGS_REDIRECT_URL)
        ),
        SettingsGroup(
            title = "Notifications",
            settings = listOf(
                SETTINGS_TWITCH_LIVE,
                SETTINGS_NEW_TWEET
            )
        )
    )
    //add credits
    SettingsScreen(
        modifier = modifier,
        innerPadding = innerPadding,
        settingsState = settingsState,
        settingsList = settingsList,
        setShouldRedirectUrl = viewModel::setShouldRedirectUrl,
        setShouldSendTwitchLive = viewModel::setShouldSendTwitchLive,
        setShouldSendNewTweet = viewModel::setShouldSendNewTweet
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
    setShouldSendNewTweet: (Boolean) -> Unit = {}
) {
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.setting_background),
            contentDescription = null,
            modifier = modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(modifier
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
            Text(text = "Settings",
                modifier = modifier.padding(horizontal = 30.dp, vertical = 20.dp),
                fontSize = 35.sp,
                color = colorResource(R.color.backgroundColor),
                fontWeight = FontWeight.W400
            )
            LazyColumn(modifier = modifier.padding(horizontal = 30.dp, vertical = 20.dp)) {
                items(settingsList.size) { index ->
                    SettingsCluster(
                        modifier = modifier,
                        settingsState = settingsState,
                        group = settingsList[index],
                        setShouldRedirectUrl = setShouldRedirectUrl,
                        setShouldSendTwitchLive = setShouldSendTwitchLive,
                        setShouldSendNewTweet = setShouldSendNewTweet
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
    setShouldSendNewTweet: (Boolean) -> Unit = {}
) {
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

@DoobyPreview
@Composable
internal fun SettingsScreenPreview() {
    SettingsScreen(
        settingsState = SettingsState.Success(
            SettingsData(
                shouldRedirectUrl = true,
                shouldSendTwitchLive = true,
                shouldSendNewTweet = false
            )
        ),
        settingsList = listOf(
            SettingsGroup(
                title = "General",
                settings = listOf(SETTINGS_REDIRECT_URL)
            ),
            SettingsGroup(
                title = "Notifications",
                settings = listOf(
                    SETTINGS_TWITCH_LIVE,
                    SETTINGS_NEW_TWEET
                )
            )
        )
    )
}

internal const val SETTINGS_REDIRECT_URL = "Redirect to External Apps"
internal const val SETTINGS_TWITCH_LIVE = "Twitch Goes Live"
internal const val SETTINGS_NEW_TWEET = "New Tweet"