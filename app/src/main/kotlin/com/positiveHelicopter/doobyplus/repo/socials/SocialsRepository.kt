package com.positiveHelicopter.doobyplus.repo.socials

import com.positiveHelicopter.doobyplus.model.PostMessage
import com.positiveHelicopter.doobyplus.model.SocialsData
import com.positiveHelicopter.doobyplus.model.SocialsVideo
import com.positiveHelicopter.doobyplus.model.database.TweetEntity
import com.positiveHelicopter.doobyplus.model.database.TwitchEntity
import com.positiveHelicopter.doobyplus.model.database.YouTubeEntity
import kotlinx.coroutines.flow.Flow

interface SocialsRepository {
    val data: Flow<SocialsData>
    suspend fun setIsFirstTimeNotification(isFirstTime: Boolean)
    suspend fun setBottomNavigationExpandedState(isExpanded: Boolean)
    fun getTweets(): Flow<List<PostMessage>>
    suspend fun insertTweets(tweets: List<TweetEntity>)
    suspend fun updateTweetPreview(id: String, text: String, url: String)
    fun getTwitchVODs(): Flow<List<SocialsVideo>>
    fun getTwitchTopClips(): Flow<List<SocialsVideo>>
    suspend fun insertTwitchVideos(videos: List<TwitchEntity>)
    suspend fun deleteOldTwitchVideos(videos: List<TwitchEntity>)
    fun getYouTubeVideos(): Flow<List<SocialsVideo>>
    fun getYouTubeShorts(): Flow<List<SocialsVideo>>
    fun getYouTubeLiveStreams(): Flow<List<SocialsVideo>>
    suspend fun insertYouTubeVideos(videos: List<YouTubeEntity>)
    suspend fun deleteOldYouTubeVideos(videos: List<YouTubeEntity>)
}