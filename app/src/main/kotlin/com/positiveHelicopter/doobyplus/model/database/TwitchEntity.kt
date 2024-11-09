package com.positiveHelicopter.doobyplus.model.database

import androidx.room.Entity
import com.positiveHelicopter.doobyplus.model.TwitchVideo

@Entity(tableName = "twitch", primaryKeys = ["id", "type"])
data class TwitchEntity(
    val id: String,
    val title: String,
    val date: String,
    val url: String,
    val thumbnailUrl: String,
    val duration: String,
    val type: String
)

fun TwitchEntity.asExternalModel() = TwitchVideo(
    title = title,
    date = date,
    url = url,
    thumbnailUrl = thumbnailUrl,
    duration = duration,
    type = type
)
