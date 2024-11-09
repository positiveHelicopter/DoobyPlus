package com.positiveHelicopter.doobyplus.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.positiveHelicopter.doobyplus.model.database.TwitchEntity

@Dao
interface TwitchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideos(videos: List<TwitchEntity>)
}