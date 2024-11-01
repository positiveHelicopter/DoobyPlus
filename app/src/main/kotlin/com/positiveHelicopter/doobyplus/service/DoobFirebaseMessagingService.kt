package com.positiveHelicopter.doobyplus.service

import com.google.firebase.messaging.FirebaseMessagingService

class DoobFirebaseMessagingService: FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        sendRegistrationToServer()
    }

    private fun sendRegistrationToServer() {
        // Send the token to the server
    }
}