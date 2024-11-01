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
            isFirstTimeNotification = preference[FIRST_TIME_NOTIFICATION] ?: true
        )
    }

    suspend fun updateIsFirstTimeNotification(isFirstTime: Boolean) {
        userPreferences.edit {
            it[FIRST_TIME_NOTIFICATION] = isFirstTime
        }
    }


}

internal val FIRST_TIME_NOTIFICATION = booleanPreferencesKey("first_time_notification")