package com.positiveHelicopter.doobyplus.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.positiveHelicopter.doobyplus.utility.notification.DEFAULT_NOTIFICATION_ID
import com.positiveHelicopter.doobyplus.utility.notification.DOOB_CHANNEL_DESCRIPTION
import com.positiveHelicopter.doobyplus.utility.notification.DOOB_CHANNEL_ID
import com.positiveHelicopter.doobyplus.utility.notification.DOOB_CHANNEL_NAME
import com.positiveHelicopter.doobyplus.utility.notification.DoobNotificationManager
import com.positiveHelicopter.doobyplus.utility.notification.TWITCH_CHANNEL_DESCRIPTION
import com.positiveHelicopter.doobyplus.utility.notification.TWITCH_CHANNEL_ID
import com.positiveHelicopter.doobyplus.utility.notification.TWITCH_CHANNEL_NAME
import com.positiveHelicopter.doobyplus.utility.notification.TWITCH_ONLINE_NOTIFICATION_ID
import com.positiveHelicopter.doobyplus.utility.notification.TWITTER_CHANNEL_DESCRIPTION
import com.positiveHelicopter.doobyplus.utility.notification.TWITTER_CHANNEL_ID
import com.positiveHelicopter.doobyplus.utility.notification.TWITTER_CHANNEL_NAME
import com.positiveHelicopter.doobyplus.utility.notification.TWITTER_POST_NOTIFICATION_ID
import com.positiveHelicopter.doobyplus.utility.notification.YOUTUBE_CHANNEL_DESCRIPTION
import com.positiveHelicopter.doobyplus.utility.notification.YOUTUBE_CHANNEL_ID
import com.positiveHelicopter.doobyplus.utility.notification.YOUTUBE_CHANNEL_NAME
import com.positiveHelicopter.doobyplus.utility.notification.YOUTUBE_ONLINE_NOTIFICATION_ID

class DoobFirebaseMessagingService: FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        sendRegistrationToServer()
        println("new token: $token")
    }

    private fun sendRegistrationToServer() {
        // Send the token to the server
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        println("message received: ${message.data}")
        val title = message.data["title"] ?: return
        val text = message.data["body"] ?: return
        val type = message.data["type"] ?: ""

        val twitch = "twitch"
        val youtube = "youtube"
        val twitter = "twitter"

        val manager = DoobNotificationManager()
        val channelId = when (type) {
            twitch -> TWITCH_CHANNEL_ID
            youtube -> YOUTUBE_CHANNEL_ID
            twitter -> TWITTER_CHANNEL_ID
            else -> DOOB_CHANNEL_ID
        }
        val channelName = when (type) {
            twitch -> TWITCH_CHANNEL_NAME
            youtube -> YOUTUBE_CHANNEL_NAME
            twitter -> TWITTER_CHANNEL_NAME
            else -> DOOB_CHANNEL_NAME
        }
        val channelDesc = when (type) {
            twitch -> TWITCH_CHANNEL_DESCRIPTION
            youtube -> YOUTUBE_CHANNEL_DESCRIPTION
            twitter -> TWITTER_CHANNEL_DESCRIPTION
            else -> DOOB_CHANNEL_DESCRIPTION
        }
        val notificationId = when (type) {
            twitch -> TWITCH_ONLINE_NOTIFICATION_ID
            youtube -> YOUTUBE_ONLINE_NOTIFICATION_ID
            twitter -> TWITTER_POST_NOTIFICATION_ID
            else -> DEFAULT_NOTIFICATION_ID
        }
        manager.createChannel(
            this,
            channelId = channelId,
            channelName = channelName,
            channelDesc = channelDesc
        )
        manager.createNotification(
            context = this,
            channelId = channelId,
            title = title,
            text = text
        ).apply {
            manager.showNotification(
                context = this@DoobFirebaseMessagingService,
                notificationId = notificationId,
                notification = this
            )
        }
    }
}