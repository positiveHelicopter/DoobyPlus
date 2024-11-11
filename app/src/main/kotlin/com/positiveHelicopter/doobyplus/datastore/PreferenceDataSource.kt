package com.positiveHelicopter.doobyplus.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.positiveHelicopter.doobyplus.model.UserPreference
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferenceDataSource @Inject constructor(
    private val userPreferences: DataStore<Preferences>
) {
    val userData = userPreferences.data.map { preference ->
        UserPreference(
            isFirstTimeNotification = preference[FIRST_TIME_NOTIFICATION] ?: true,
            shouldRedirectUrl = preference[SHOULD_REDIRECT_URL] ?: true,
            shouldSendTwitchLive = preference[SHOULD_SEND_TWITCH_LIVE] ?: true,
            shouldSendNewTweet = preference[SHOULD_SEND_NEW_TWEET] ?: true
        )
    }

    suspend fun updateIsFirstTimeNotification(isFirstTime: Boolean) {
        userPreferences.edit {
            it[FIRST_TIME_NOTIFICATION] = isFirstTime
        }
    }

    suspend fun updateShouldRedirectUrl(shouldRedirectUrl: Boolean) {
        userPreferences.edit {
            it[SHOULD_REDIRECT_URL] = shouldRedirectUrl
        }
    }

    suspend fun updateShouldSendTwitchLive(shouldSendTwitchLive: Boolean) {
        userPreferences.edit {
            it[SHOULD_SEND_TWITCH_LIVE] = shouldSendTwitchLive
        }
    }

    suspend fun updateShouldSendNewTweet(shouldSendNewTweet: Boolean) {
        userPreferences.edit {
            it[SHOULD_SEND_NEW_TWEET] = shouldSendNewTweet
        }
    }
}

internal val FIRST_TIME_NOTIFICATION = booleanPreferencesKey("first_time_notification")
internal val SHOULD_REDIRECT_URL = booleanPreferencesKey("should_redirect_url")
internal val SHOULD_SEND_TWITCH_LIVE = booleanPreferencesKey("should_send_twitch_live")
internal val SHOULD_SEND_NEW_TWEET = booleanPreferencesKey("should_send_new_tweet")