package com.positiveHelicopter.doobyplus.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.positiveHelicopter.doobyplus.model.SettingsData
import com.positiveHelicopter.doobyplus.repo.settings.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
): ViewModel() {
    val uiState: StateFlow<SettingsState> = settingsRepository.data.map {
        SettingsState.Success(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SettingsState.Loading
    )

    fun setShouldRedirectUrl(shouldRedirectUrl: Boolean) {
        viewModelScope.launch {
            settingsRepository.setShouldRedirectUrl(shouldRedirectUrl)
        }
    }

    fun setShouldSendTwitchLive(shouldSendTwitchLive: Boolean) {
        viewModelScope.launch {
            settingsRepository.setShouldSendTwitchLive(shouldSendTwitchLive)
        }
    }

    fun setShouldSendNewTweet(shouldSendNewTweet: Boolean) {
        viewModelScope.launch {
            settingsRepository.setShouldSendNewTweet(shouldSendNewTweet)
        }
    }
}

sealed interface SettingsState {
    data object Loading: SettingsState
    data class Success(val data: SettingsData): SettingsState
}