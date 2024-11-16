package com.positiveHelicopter.doobyplus.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.positiveHelicopter.doobyplus.model.SocialsVideo

@Entity(tableName = "youtube")
data class YouTubeEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val date: String,
    val url: String,
    val thumbnailUrl: String,
    val type: String
)

fun YouTubeEntity.asExternalModel() = SocialsVideo(
    title = title,
    date = date,
    url = url,
    thumbnailUrl = thumbnailUrl,
    duration = "",
    type = type
)