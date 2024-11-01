package com.positiveHelicopter.doobyplus.repo.di

import com.positiveHelicopter.doobyplus.repo.socials.DoobSocialsRepository
import com.positiveHelicopter.doobyplus.repo.socials.SocialsRepository
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
}