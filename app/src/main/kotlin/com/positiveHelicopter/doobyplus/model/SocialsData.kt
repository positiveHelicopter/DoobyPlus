package com.positiveHelicopter.doobyplus.model

data class SocialsData(
    val userPreference: UserPreference,
    val tweets: List<PostMessage>,
    val twitchVODs: List<TwitchVideo>
)