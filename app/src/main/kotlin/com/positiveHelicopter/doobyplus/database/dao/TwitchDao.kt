package com.positiveHelicopter.doobyplus.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.positiveHelicopter.doobyplus.model.database.TwitchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TwitchDao {
    @Query("SELECT * FROM twitch WHERE type = 'vods'")
    fun getTwitchVODs(): Flow<List<TwitchEntity>>

    @Query("SELECT * FROM twitch WHERE type = 'clips' OR type = 'weekly'")
    fun getTwitchTopClips(): Flow<List<TwitchEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideos(videos: List<TwitchEntity>)

    @Query("DELETE FROM twitch WHERE type = 'weekly' AND id NOT IN (:ids)")
    suspend fun deleteOldWeeklyVideos(ids: List<String>)

    @Query("DELETE FROM twitch WHERE id NOT IN (:ids)")
    suspend fun deleteOldVideos(ids: List<String>)
}