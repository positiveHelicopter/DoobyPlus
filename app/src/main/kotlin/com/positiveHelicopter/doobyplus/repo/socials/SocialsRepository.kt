package com.positiveHelicopter.doobyplus.repo.socials

import com.positiveHelicopter.doobyplus.model.SocialsData
import kotlinx.coroutines.flow.Flow

interface SocialsRepository {
    val data: Flow<SocialsData>
    suspend fun setIsFirstTimeNotification(isFirstTime: Boolean)
}