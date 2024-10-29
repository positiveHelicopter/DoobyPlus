package com.positiveHelicopter.doobyplus.screens.watch

import android.view.View
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class WatchViewModel @Inject constructor(): ViewModel() {
    private var _uiState = MutableStateFlow<WatchState>(WatchState.Default)
    val uiState = _uiState.asStateFlow()

    fun enterFullScreen(view: View?) {
        _uiState.value = WatchState.FullScreen(view)
    }

    fun exitFullScreen() {
        _uiState.value = WatchState.Default
    }
}

sealed interface WatchState {
    data object Default: WatchState
    data class FullScreen(val view: View?): WatchState
}