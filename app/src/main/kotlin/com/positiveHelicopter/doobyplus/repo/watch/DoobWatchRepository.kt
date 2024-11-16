package com.positiveHelicopter.doobyplus.repo.watch

import com.positiveHelicopter.doobyplus.datastore.PreferenceDataSource
import com.positiveHelicopter.doobyplus.model.UserPreference
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DoobWatchRepository @Inject constructor(
    userPreferenceDataSource: PreferenceDataSource
): WatchRepository {
    override val data: Flow<UserPreference> = userPreferenceDataSource.userData
}