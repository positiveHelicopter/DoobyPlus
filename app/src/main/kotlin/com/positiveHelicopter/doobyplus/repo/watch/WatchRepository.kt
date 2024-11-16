package com.positiveHelicopter.doobyplus.repo.watch

import com.positiveHelicopter.doobyplus.model.UserPreference
import kotlinx.coroutines.flow.Flow

interface WatchRepository {
    val data: Flow<UserPreference>
}