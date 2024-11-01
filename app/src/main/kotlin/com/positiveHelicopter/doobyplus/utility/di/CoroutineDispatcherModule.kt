package com.positiveHelicopter.doobyplus.utility.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import com.positiveHelicopter.doobyplus.utility.di.DispatcherType.IO

@Module
@InstallIn(SingletonComponent::class)
object CoroutineDispatcherModule {
    @Provides
    @Dispatcher(IO)
    fun provideIODispatcher() = Dispatchers.IO
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class Dispatcher(val type: DispatcherType)

enum class DispatcherType {
    IO
}