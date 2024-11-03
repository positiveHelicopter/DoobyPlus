package com.positiveHelicopter.doobyplus.database.di

import android.content.Context
import androidx.room.Room
import com.positiveHelicopter.doobyplus.database.DoobDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Provides
    @Singleton
    fun providesDoobDatabase(
        @ApplicationContext context: Context
    ): DoobDatabase = Room.databaseBuilder(
        context,
        DoobDatabase::class.java,
        "doob-database"
    ).build()
}