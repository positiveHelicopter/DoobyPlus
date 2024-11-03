package com.positiveHelicopter.doobyplus.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.positiveHelicopter.doobyplus.model.PostMessage

@Entity(tableName = "tweets")
data class TweetEntity(
    @PrimaryKey
    val timestamp: Long,
    val text: String,
    val url: String,
    val date: String
)

fun TweetEntity.asExternalModel() = PostMessage(
    text = text,
    url = url
)