package com.positiveHelicopter.doobyplus.repo.socials

import com.positiveHelicopter.doobyplus.model.PostMessage
import com.positiveHelicopter.doobyplus.model.SocialsData
import com.positiveHelicopter.doobyplus.model.TwitchVideo
import com.positiveHelicopter.doobyplus.model.database.TweetEntity
import com.positiveHelicopter.doobyplus.model.database.TwitchEntity
import kotlinx.coroutines.flow.Flow

interface SocialsRepository {
    val data: Flow<SocialsData>
    suspend fun setIsFirstTimeNotification(isFirstTime: Boolean)
    fun getTweets(): Flow<List<PostMessage>>
    suspend fun insertTweets(tweets: List<TweetEntity>)
    fun getTwitchVODs(): Flow<List<TwitchVideo>>
    suspend fun insertTwitchVideos(videos: List<TwitchEntity>)
}