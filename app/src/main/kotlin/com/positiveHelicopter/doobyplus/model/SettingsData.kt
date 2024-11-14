package com.positiveHelicopter.doobyplus.model

data class SettingsData(
    val shouldRedirectUrl: Boolean,
    val shouldSendTwitchLive: Boolean,
    val shouldSendNewTweet: Boolean,
    val shouldSendYoutubeUpload: Boolean,
    val isCredits: Boolean
)
