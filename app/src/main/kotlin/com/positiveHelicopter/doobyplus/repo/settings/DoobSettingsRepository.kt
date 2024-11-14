package com.positiveHelicopter.doobyplus.repo.settings

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.positiveHelicopter.doobyplus.datastore.PreferenceDataSource
import com.positiveHelicopter.doobyplus.model.SettingsData
import com.positiveHelicopter.doobyplus.utility.di.Dispatcher
import com.positiveHelicopter.doobyplus.utility.di.DispatcherType.IO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DoobSettingsRepository @Inject constructor(
    @Dispatcher(IO) private val dispatcher: CoroutineDispatcher,
    private val userPreferenceDataSource: PreferenceDataSource
) : SettingsRepository {
    override val data: Flow<SettingsData> = userPreferenceDataSource.userData.map {
        SettingsData(
            shouldRedirectUrl = it.shouldRedirectUrl,
            shouldSendTwitchLive = it.shouldSendTwitchLive,
            shouldSendNewTweet = it.shouldSendNewTweet,
            shouldSendYoutubeUpload = it.shouldSendYoutubeUpload,
            isCredits = false
        )
    }

    override suspend fun setShouldRedirectUrl(
        shouldRedirectUrl: Boolean
    ) = withContext(dispatcher) {
        userPreferenceDataSource.updateShouldRedirectUrl(shouldRedirectUrl)
    }

    override suspend fun setShouldSendTwitchLive(
        shouldSendTwitchLive: Boolean
    ) = withContext(dispatcher) {
        FirebaseMessaging.getInstance().apply {
            if (shouldSendTwitchLive) {
                subscribeToTopic("twitch_stream_online").addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.i(
                            "DoobSettingsRepository",
                            "Subscribed to twitch_stream_online"
                        )
                    } else {
                        CoroutineScope(dispatcher).launch {
                            userPreferenceDataSource.updateShouldSendTwitchLive(false)
                        }
                    }
                }
            } else {
                unsubscribeFromTopic("twitch_stream_online").addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.i(
                            "DoobSettingsRepository",
                            "Unsubscribed from twitch_stream_online"
                        )
                    } else {
                        CoroutineScope(dispatcher).launch {
                            userPreferenceDataSource.updateShouldSendTwitchLive(true)
                        }
                    }
                }
            }
        }
        userPreferenceDataSource.updateShouldSendTwitchLive(shouldSendTwitchLive)
    }

    override suspend fun setShouldSendNewTweet(
        shouldSendNewTweet: Boolean
    ) = withContext(dispatcher) {
        FirebaseMessaging.getInstance().apply {
            if (shouldSendNewTweet) {
                subscribeToTopic("twitter_post").addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.i(
                            "DoobSettingsRepository",
                            "Subscribed to twitter_post"
                        )
                    } else {
                        CoroutineScope(dispatcher).launch {
                            userPreferenceDataSource.updateShouldSendNewTweet(false)
                        }
                    }
                }
            } else {
                unsubscribeFromTopic("twitter_post").addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.i(
                            "DoobSettingsRepository",
                            "Unsubscribed from twitter_post"
                        )
                    } else {
                        CoroutineScope(dispatcher).launch {
                            userPreferenceDataSource.updateShouldSendNewTweet(true)
                        }
                    }
                }
            }
        }
        userPreferenceDataSource.updateShouldSendNewTweet(shouldSendNewTweet)
    }

    override suspend fun setShouldSendYoutubeUpload(
        shouldSendYoutubeUpload: Boolean
    ) = withContext(dispatcher) {
        FirebaseMessaging.getInstance().apply {
            if (shouldSendYoutubeUpload) {
                subscribeToTopic("youtube_upload").addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.i(
                            "DoobSettingsRepository",
                            "Subscribed to youtube_upload"
                        )
                    } else {
                        CoroutineScope(dispatcher).launch {
                            userPreferenceDataSource.updateShouldSendYoutubeUpload(false)
                        }
                    }
                }
            } else {
                unsubscribeFromTopic("youtube_upload").addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.i(
                            "DoobSettingsRepository",
                            "Unsubscribed from youtube_upload"
                        )
                    } else {
                        CoroutineScope(dispatcher).launch {
                            userPreferenceDataSource.updateShouldSendYoutubeUpload(true)
                        }
                    }
                }
            }
        }
        userPreferenceDataSource.updateShouldSendYoutubeUpload(shouldSendYoutubeUpload)
    }
}