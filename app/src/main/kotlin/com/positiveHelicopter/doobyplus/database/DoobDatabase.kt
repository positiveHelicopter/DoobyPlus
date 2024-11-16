package com.positiveHelicopter.doobyplus.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.positiveHelicopter.doobyplus.database.dao.TweetDao
import com.positiveHelicopter.doobyplus.database.dao.TwitchDao
import com.positiveHelicopter.doobyplus.database.dao.YouTubeDao
import com.positiveHelicopter.doobyplus.model.database.TweetEntity
import com.positiveHelicopter.doobyplus.model.database.TwitchEntity
import com.positiveHelicopter.doobyplus.model.database.YouTubeEntity

@Database(
    entities = [TweetEntity::class, TwitchEntity::class, YouTubeEntity::class],
    version = 1
)

internal abstract class DoobDatabase: RoomDatabase() {
    abstract fun tweetDao(): TweetDao
    abstract fun twitchDao(): TwitchDao
    abstract fun youtubeDao(): YouTubeDao
}