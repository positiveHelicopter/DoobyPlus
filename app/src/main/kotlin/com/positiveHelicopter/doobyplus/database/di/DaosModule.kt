package com.positiveHelicopter.doobyplus.database.di

import com.positiveHelicopter.doobyplus.database.DoobDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {
    @Provides
    fun providesTweetDao(
        doobDatabase: DoobDatabase
    ) = doobDatabase.tweetDao()
}