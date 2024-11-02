package com.positiveHelicopter.doobyplus.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

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
    }
}