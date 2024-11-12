package com.positiveHelicopter.doobyplus.screens.socials

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.positiveHelicopter.doobyplus.model.SocialsData
import com.positiveHelicopter.doobyplus.repo.socials.SocialsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SocialsViewModel @Inject constructor(
    private val socialsRepository: SocialsRepository
): ViewModel() {
    val uiState: StateFlow<SocialsState> = socialsRepository.data.map {
        SocialsState.Success(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SocialsState.Loading
    )

    fun setIsFirstTimeNotification(isFirstTime: Boolean) {
        viewModelScope.launch {
            socialsRepository.setIsFirstTimeNotification(isFirstTime)
        }
    }

    fun updateTweetPreview(id: String, url: String) {
        viewModelScope.launch {
            socialsRepository.updateTweetPreview(id, url)
        }
    }
}

sealed interface SocialsState {
    data object Loading: SocialsState
    data class Success(val data: SocialsData): SocialsState
}