package com.positiveHelicopter.doobyplus.model

data class SocialsData(
    val userPreference: UserPreference,
    val previewImage: PreviewImage,
    val tweets: List<PostMessage>,
    val twitchVODs: List<SocialsVideo>,
    val twitchTopClips: List<SocialsVideo>,
    val youtubeVideos: List<SocialsVideo>,
    val youtubeShorts: List<SocialsVideo>,
    val youtubeLiveStreams: List<SocialsVideo>
)

data class PreviewImage(
    val shouldPreviewImage: Boolean,
    val url: String
)