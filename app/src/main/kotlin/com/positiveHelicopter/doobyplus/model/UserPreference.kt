package com.positiveHelicopter.doobyplus.model

data class UserPreference(
    val isFirstTimeNotification: Boolean,
    val shouldRedirectUrl: Boolean,
    val shouldSendTwitchLive: Boolean,
    val shouldSendNewTweet: Boolean,
    val shouldSendYoutubeUpload: Boolean,
    val bottomNavigationExpandedState: Boolean
)
