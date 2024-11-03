package com.positiveHelicopter.doobyplus.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.positiveHelicopter.doobyplus.database.dao.TweetDao
import com.positiveHelicopter.doobyplus.model.database.TweetEntity

@Database(
    entities = [TweetEntity::class],
    version = 1
)

internal abstract class DoobDatabase: RoomDatabase() {
    abstract fun tweetDao(): TweetDao
}