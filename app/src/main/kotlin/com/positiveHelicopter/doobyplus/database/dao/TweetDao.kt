package com.positiveHelicopter.doobyplus.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.positiveHelicopter.doobyplus.model.database.TweetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TweetDao {
    @Query("SELECT * FROM tweets")
    fun getTweets(): Flow<List<TweetEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTweets(tweets: List<TweetEntity>)

    @Query("UPDATE tweets SET previewLink = :previewLink WHERE id = :id")
    suspend fun updateTweetPreview(id: String, previewLink: String)
}