package com.positiveHelicopter.doobyplus.network

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import com.positiveHelicopter.doobyplus.model.database.TweetEntity
import com.positiveHelicopter.doobyplus.repo.socials.SocialsRepository
import com.positiveHelicopter.doobyplus.utility.di.Dispatcher
import com.positiveHelicopter.doobyplus.utility.di.DispatcherType.IO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class FireStoreUpdateManager @Inject constructor(
    @Dispatcher(IO) private val coroutineDispatcher: CoroutineDispatcher,
    private val socialsRepository: SocialsRepository
) {
    private val registrations = mutableListOf<ListenerRegistration>()
    fun listenForUpdates() {
        listenForTweets()
    }

    fun unregisterListeners() {
        registrations.forEach { it.remove() }
    }

    private fun listenForTweets() {
        listenForUpdate("tweets") { snapshot ->
            val entries = snapshot.data?.map { tweet ->
                val obj = tweet.value as Map<*, *>
                TweetEntity(
                    id = tweet.key,
                    text = obj["text"] as String? ?: "",
                    url = "",
                    date = obj["date"] as String? ?: "",
                    timestamp = obj["timestamp"] as Long? ?: -1,
                    link = obj["link"] as String? ?: ""
                )
            } ?: emptyList()
            socialsRepository.insertTweets(entries)
        }
    }

    private fun listenForUpdate(
        docName: String,
        operation: suspend (DocumentSnapshot) -> Unit
    ) {
        val db = Firebase.firestore("doobyplus")
        val collection = db.collection("dooby")
        val tweets = collection.document(docName)
        val registration = tweets.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.e("TweetFireStoreListener", "Listen failed", exception)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                Log.d("TweetFireStoreListener", "Current data: ${snapshot.data}")
                CoroutineScope(coroutineDispatcher).launch {
                    operation(snapshot)
                }
            } else {
                Log.d("TweetFireStoreListener", "Current data: null")
            }
        }
        registrations.add(registration)
    }
}