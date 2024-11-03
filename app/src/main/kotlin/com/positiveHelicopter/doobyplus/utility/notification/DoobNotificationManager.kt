package com.positiveHelicopter.doobyplus.utility.notification

import android.app.Notification
import android.app.NotificationChannel
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.positiveHelicopter.doobyplus.R
import android.Manifest
import android.os.Build

class DoobNotificationManager: NotificationManager {
    override fun createChannel(
        context: Context,
        channelId: String,
        channelName: String,
        channelDesc: String
    ) {
        val importance = android.app.NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, channelName, importance).apply {
            description = channelDesc
        }
        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE)
                as android.app.NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    override fun createNotification(
        context: Context,
        channelId: String,
        title: String,
        text: String
    ) = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_train_black)
        .setColor(context.getColor(R.color.colorPrimary))
        .setContentTitle(title)
        .setContentText(text)
        .setStyle(NotificationCompat.BigTextStyle()
            .bigText(text))
        .build()

    override fun showNotification(
        context: Context,
        notificationId: Int,
        notification: Notification
    ) {
        val hasPermission = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            true
        } else {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        }
        if (!hasPermission) return
        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, notification)
        }
    }
}

const val DOOB_CHANNEL_ID = "doob_channel_id"
const val DOOB_CHANNEL_NAME = "DoobyPlus"
const val DOOB_CHANNEL_DESCRIPTION = "DoobyPlus notifications"

const val TWITCH_CHANNEL_ID = "twitch_channel_id"
const val TWITCH_CHANNEL_NAME = "Twitch"
const val TWITCH_CHANNEL_DESCRIPTION = "Twitch notifications"

const val YOUTUBE_CHANNEL_ID = "youtube_channel_id"
const val YOUTUBE_CHANNEL_NAME = "Youtube"
const val YOUTUBE_CHANNEL_DESCRIPTION = "Youtube notifications"

const val TWITTER_CHANNEL_ID = "twitter_channel_id"
const val TWITTER_CHANNEL_NAME = "X"
const val TWITTER_CHANNEL_DESCRIPTION = "X notifications"

const val DEFAULT_NOTIFICATION_ID = 0
const val TWITCH_ONLINE_NOTIFICATION_ID = 1
const val YOUTUBE_ONLINE_NOTIFICATION_ID = 2
const val TWITTER_POST_NOTIFICATION_ID = 3