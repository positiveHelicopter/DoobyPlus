package com.positiveHelicopter.doobyplus.model

data class PostMessage(
    val id: String,
    val text: String,
    val date: String,
    val timestamp: Long,
    val link: String,
    val previewLink: String
)
