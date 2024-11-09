package com.positiveHelicopter.doobyplus.network

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import com.positiveHelicopter.doobyplus.model.database.TweetEntity
import com.positiveHelicopter.doobyplus.model.database.TwitchEntity
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
        listenForTwitch()
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
                    date = (obj["date"] as String?)?.trim() ?: "",
                    timestamp = obj["timestamp"] as Long? ?: -1,
                    link = (obj["link"] as String?)?.trim() ?: ""
                )
            } ?: emptyList()
            socialsRepository.insertTweets(entries)
        }
    }

    private fun listenForTwitch() {
        listenForUpdate("twitch") { snapshot ->
            val videos = mutableListOf<TwitchEntity>()
            snapshot.data?.forEach { (type, value) ->
                val obj = value as ArrayList<*>
                obj.forEach {
                    val video = it as Map<*, *>
                    videos.add(
                        TwitchEntity(
                            id = video["id"] as String? ?: "",
                            title = video["title"] as String? ?: "",
                            date = video["created_at "] as String? ?: "",
                            url = video["url"] as String? ?: "",
                            thumbnailUrl = video["thumbnail_url"] as String? ?: "",
                            duration = video["duration"] as String? ?: "",
                            type = type
                        )
                    )
                }
            }
            socialsRepository.insertVideos(videos)
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
                Log.e("FireStoreListener", "Listen failed", exception)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                Log.d("FireStoreListener", "Current data: ${snapshot.data}")
                CoroutineScope(coroutineDispatcher).launch {
                    operation(snapshot)
                }
            } else {
                Log.d("FireStoreListener", "Current data: null")
            }
        }
        registrations.add(registration)
    }
}