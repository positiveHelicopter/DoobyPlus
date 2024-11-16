package com.positiveHelicopter.doobyplus.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.positiveHelicopter.doobyplus.model.database.YouTubeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface YouTubeDao {
    @Query("SELECT * FROM youtube WHERE type = 'video'")
    fun getYouTubeVideos(): Flow<List<YouTubeEntity>>

    @Query("SELECT * FROM youtube WHERE type = 'shorts'")
    fun getYouTubeShorts(): Flow<List<YouTubeEntity>>

    @Query("SELECT * FROM youtube WHERE type = 'live'")
    fun getYouTubeLiveStreams(): Flow<List<YouTubeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideos(videos: List<YouTubeEntity>)

    @Query("DELETE FROM youtube WHERE id NOT IN (:ids)")
    suspend fun deleteOldVideos(ids: List<String>)
}