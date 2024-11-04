package com.positiveHelicopter.doobyplus

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.positiveHelicopter.doobyplus.sync.Sync
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class DoobApplication : Application(), Configuration.Provider {
    @Inject lateinit var workFactory: HiltWorkerFactory
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        Sync.initialize(this)
    }
}