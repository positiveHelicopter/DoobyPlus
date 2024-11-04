package com.positiveHelicopter.doobyplus.sync

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager

object Sync {
    fun initialize(context: Context) {
        WorkManager.getInstance(context).apply {
            enqueueUniqueWork(
                SYNC_WORK_NAME,
                ExistingWorkPolicy.KEEP,
                SyncWorker.startSyncWork()
            )
        }
    }
}

internal const val SYNC_WORK_NAME = "SyncWork"