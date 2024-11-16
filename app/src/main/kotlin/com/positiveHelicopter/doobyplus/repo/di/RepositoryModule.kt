package com.positiveHelicopter.doobyplus.repo.di

import com.positiveHelicopter.doobyplus.repo.settings.DoobSettingsRepository
import com.positiveHelicopter.doobyplus.repo.settings.SettingsRepository
import com.positiveHelicopter.doobyplus.repo.socials.DoobSocialsRepository
import com.positiveHelicopter.doobyplus.repo.socials.SocialsRepository
import com.positiveHelicopter.doobyplus.repo.watch.DoobWatchRepository
import com.positiveHelicopter.doobyplus.repo.watch.WatchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    internal abstract fun bindsSocialsRepository(
        socialsRepository: DoobSocialsRepository,
    ): SocialsRepository

    @Binds
    internal abstract fun bindsSettingsRepository(
        settingsRepository: DoobSettingsRepository,
    ): SettingsRepository

    @Binds
    internal abstract fun bindsWatchRepository(
        watchRepository: DoobWatchRepository,
    ): WatchRepository
}