package com.positiveHelicopter.doobyplus.sync

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkerParameters
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.messaging.FirebaseMessaging
import com.positiveHelicopter.doobyplus.model.database.TweetEntity
import com.positiveHelicopter.doobyplus.repo.socials.SocialsRepository
import com.positiveHelicopter.doobyplus.utility.di.Dispatcher
import com.positiveHelicopter.doobyplus.utility.di.DispatcherType.IO
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
): CoroutineWorker(appContext, workerParams) {
    @Inject lateinit var socialsRepository: SocialsRepository
    override suspend fun doWork(): Result = withContext(ioDispatcher) {
        getCurrentFirebaseToken()
        val result = awaitAll(
            async { getFireStoreData() }
        ).all { it }
        if(result) Result.success() else Result.failure()
    }

    private fun getFireStoreData(): Boolean {
        val timeout = 10_000
        var isSuccess = true
        var isFetching = true
        val startTime = System.currentTimeMillis()
        val db = Firebase.firestore("doobyplus")
        val collection = db.collection("dooby")
        val tweets = collection.document("tweets")
        tweets.get()
            .addOnSuccessListener {
                Log.i("SyncWorker", "Tweets: $it")
                CoroutineScope(ioDispatcher).launch {
                    val entries = it.data?.map { tweet ->
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
                isFetching = false
            }
            .addOnFailureListener {
                Log.e("SyncWorker", "Error getting tweets", it)
                isSuccess = false
                isFetching = false
            }
        while(isFetching) {
            if (System.currentTimeMillis() - startTime > timeout) {
                isFetching = false
                isSuccess = false
                Log.e("SyncWorker", "Timeout")
            }
        }
        return isSuccess
    }

    private fun getCurrentFirebaseToken() {
        FirebaseMessaging.getInstance().apply {
            token.addOnCompleteListener { _ -> }
            subscribeToTopic("twitch_stream_online").addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.i("SyncWorker", "Subscribed to twitch_stream_online")
                }
            }
            subscribeToTopic("twitter_post").addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.i("SyncWorker", "Subscribed to twitter_post")
                }
            }
        }
    }

    companion object {
        fun startSyncWork() = OneTimeWorkRequestBuilder<SyncWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setConstraints(Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build())
            .build()
    }
}