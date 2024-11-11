package com.positiveHelicopter.doobyplus.repo.settings

import com.positiveHelicopter.doobyplus.datastore.PreferenceDataSource
import com.positiveHelicopter.doobyplus.model.SettingsData
import com.positiveHelicopter.doobyplus.utility.di.Dispatcher
import com.positiveHelicopter.doobyplus.utility.di.DispatcherType.IO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DoobSettingsRepository @Inject constructor(
    @Dispatcher(IO) private val dispatcher: CoroutineDispatcher,
    private val userPreferenceDataSource: PreferenceDataSource
): SettingsRepository {
    override val data: Flow<SettingsData> = userPreferenceDataSource.userData.map {
        SettingsData(
            shouldRedirectUrl = it.shouldRedirectUrl,
            shouldSendTwitchLive = it.shouldSendTwitchLive,
            shouldSendNewTweet = it.shouldSendNewTweet
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
        userPreferenceDataSource.updateShouldSendTwitchLive(shouldSendTwitchLive)
    }

    override suspend fun setShouldSendNewTweet(
        shouldSendNewTweet: Boolean
    ) = withContext(dispatcher) {
        userPreferenceDataSource.updateShouldSendNewTweet(shouldSendNewTweet)
    }
}