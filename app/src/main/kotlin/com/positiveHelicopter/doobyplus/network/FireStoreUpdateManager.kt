package com.positiveHelicopter.doobyplus.network

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import com.positiveHelicopter.doobyplus.model.database.TweetEntity
import com.positiveHelicopter.doobyplus.model.database.TwitchEntity
import com.positiveHelicopter.doobyplus.model.database.YouTubeEntity
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
        listenForYoutube()
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
                    text = obj["text"].toString(),
                    date = obj["date"].toString().trim(),
                    timestamp = obj["timestamp"] as Long? ?: -1,
                    link = obj["link"].toString().trim(),
                    previewLink = ""
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
                    val thumbnailUrl = video["thumbnail_url"].toString()
                        .replace("%{width}", "320")
                        .replace("%{height}", "180")
                    videos.add(
                        TwitchEntity(
                            id = video["id"].toString(),
                            title = video["title"].toString(),
                            date = video["created_at"].toString(),
                            url = video["url"].toString(),
                            thumbnailUrl = thumbnailUrl,
                            duration = video["duration"].toString(),
                            type = type
                        )
                    )
                }
            }
            socialsRepository.deleteOldTwitchVideos(videos)
            socialsRepository.insertTwitchVideos(videos)
        }
    }

    private fun listenForYoutube() {
        listenForUpdate("youtube") { snapshot ->
            val entries = snapshot.data?.map { video ->
                val obj = video.value as Map<*, *>
                val url = "https://www.youtube.com/watch?v=${video.key}"
                YouTubeEntity(
                    id = video.key,
                    title = obj["title"].toString(),
                    date = obj["date"].toString().trim(),
                    url = url,
                    thumbnailUrl = obj["thumbnail"].toString().trim(),
                    type = obj["type"].toString().trim()
                )
            } ?: emptyList()
            socialsRepository.deleteOldYouTubeVideos(entries)
            socialsRepository.insertYouTubeVideos(entries)
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