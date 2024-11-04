package com.positiveHelicopter.doobyplus.repo.socials

import com.positiveHelicopter.doobyplus.model.PostMessage
import com.positiveHelicopter.doobyplus.model.SocialsData
import com.positiveHelicopter.doobyplus.model.database.TweetEntity
import kotlinx.coroutines.flow.Flow

interface SocialsRepository {
    val data: Flow<SocialsData>
    suspend fun setIsFirstTimeNotification(isFirstTime: Boolean)
    fun getTweets(): Flow<List<PostMessage>>
    suspend fun insertTweets(tweets: List<TweetEntity>)
}