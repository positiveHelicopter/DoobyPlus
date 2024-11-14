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
import com.google.firebase.messaging.FirebaseMessaging
import com.positiveHelicopter.doobyplus.repo.socials.SocialsRepository
import com.positiveHelicopter.doobyplus.utility.di.Dispatcher
import com.positiveHelicopter.doobyplus.utility.di.DispatcherType.IO
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
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
        Result.success()
    }

    private suspend fun getCurrentFirebaseToken() {
        val userPreference = socialsRepository.data.first().userPreference
        FirebaseMessaging.getInstance().apply {
            token.addOnCompleteListener { _ -> }
            if (userPreference.shouldSendTwitchLive)
                subscribeToTopic("twitch_stream_online").addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.i("SyncWorker", "Subscribed to twitch_stream_online")
                    }
                }
            if (userPreference.shouldSendNewTweet)
                subscribeToTopic("twitter_post").addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.i("SyncWorker", "Subscribed to twitter_post")
                    }
                }
            if (userPreference.shouldSendYoutubeUpload) {
                subscribeToTopic("youtube_upload").addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.i("SyncWorker", "Subscribed to youtube_upload")
                    }
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