package com.positiveHelicopter.doobyplus.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.positiveHelicopter.doobyplus.model.PostMessage

@Entity(tableName = "tweets")
data class TweetEntity(
    @PrimaryKey
    val id: String,
    val text: String,
    val date: String,
    val timestamp: Long,
    val link: String,
    val previewLink: String
)

fun TweetEntity.asExternalModel() = PostMessage(
    id = id,
    text = text,
    date = date,
    timestamp = timestamp,
    link = link,
    previewLink = previewLink
)