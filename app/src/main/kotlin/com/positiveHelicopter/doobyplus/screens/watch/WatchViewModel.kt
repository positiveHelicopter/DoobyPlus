package com.positiveHelicopter.doobyplus.screens.watch

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.positiveHelicopter.doobyplus.model.WatchData
import com.positiveHelicopter.doobyplus.repo.watch.WatchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import java.lang.ref.WeakReference
import javax.inject.Inject

@HiltViewModel
class WatchViewModel @Inject constructor(
    watchRepository: WatchRepository
): ViewModel() {
    private var fullScreenView: WeakReference<View>? = null
    private val fullScreenFlow = flow {
        while (true) {
            emit(fullScreenView?.get())
        }
    }
    val uiState = combine(
        fullScreenFlow,
        watchRepository.data
    ) { view, data ->
        WatchState.Success(
            WatchData(
                userPreference = data,
                view = view
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = WatchState.Default
    )

    fun enterFullScreen(view: View?) {
        fullScreenView = WeakReference(view)
    }

    fun exitFullScreen() {
        fullScreenView = null
    }
}

sealed interface WatchState {
    data object Default: WatchState
    data class Success(val data: WatchData): WatchState
}