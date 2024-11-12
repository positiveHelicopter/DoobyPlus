package com.positiveHelicopter.doobyplus.model

data class SocialsData(
    val userPreference: UserPreference,
    val previewImage: PreviewImage,
    val tweets: List<PostMessage>,
    val twitchVODs: List<TwitchVideo>,
    val twitchTopClips: List<TwitchVideo>
)

data class PreviewImage(
    val shouldPreviewImage: Boolean,
    val url: String
)