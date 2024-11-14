package com.positiveHelicopter.doobyplus.repo.settings

import com.positiveHelicopter.doobyplus.model.SettingsData
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val data: Flow<SettingsData>
    suspend fun setShouldRedirectUrl(shouldRedirectUrl: Boolean)
    suspend fun setShouldSendTwitchLive(shouldSendTwitchLive: Boolean)
    suspend fun setShouldSendNewTweet(shouldSendNewTweet: Boolean)
    suspend fun setShouldSendYoutubeUpload(shouldSendYoutubeUpload: Boolean)
}