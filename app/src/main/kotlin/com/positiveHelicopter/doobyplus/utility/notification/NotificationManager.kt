package com.positiveHelicopter.doobyplus.utility.notification

import android.app.Notification
import android.content.Context

interface NotificationManager {
    fun createChannel(
        context: Context,
        channelId: String,
        channelName: String,
        channelDesc: String
    )
    fun createNotification(
        context: Context,
        channelId: String,
        title: String,
        text: String
    ): Notification
    fun showNotification(
        context: Context,
        notificationId: Int,
        notification: Notification
    )
}