package com.positiveHelicopter.doobyplus.screens.socials

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.positiveHelicopter.doobyplus.model.PreviewImage
import com.positiveHelicopter.doobyplus.model.SocialsData
import com.positiveHelicopter.doobyplus.repo.socials.SocialsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SocialsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val socialsRepository: SocialsRepository
): ViewModel() {
    private val shouldPreviewImageKey = "shouldPreviewImageKey"
    private val previewImageUrlKey = "previewImageUrlKey"
    private val shouldPreviewImage = savedStateHandle.getStateFlow(
        key = shouldPreviewImageKey,
        initialValue = false
    )
    private val previewImageUrl = savedStateHandle.getStateFlow(
        key = previewImageUrlKey,
        initialValue = ""
    )

    val uiState: StateFlow<SocialsState> = combine(
        socialsRepository.data,
        shouldPreviewImage,
        previewImageUrl,
        transform = { data, shouldPreview, imageUrl ->
            SocialsState.Success(data.copy(
                previewImage = PreviewImage(
                    shouldPreviewImage = shouldPreview,
                    url = imageUrl
                )
            ))
        }
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SocialsState.Loading
    )

    fun setIsFirstTimeNotification(isFirstTime: Boolean) {
        viewModelScope.launch {
            socialsRepository.setIsFirstTimeNotification(isFirstTime)
        }
    }

    fun setBottomNavigationExpandedState(isExpanded: Boolean) {
        viewModelScope.launch {
            socialsRepository.setBottomNavigationExpandedState(isExpanded)
        }
    }

    fun updateTweetPreview(id: String, text: String, url: String) {
        viewModelScope.launch {
            socialsRepository.updateTweetPreview(id, text, url)
        }
    }

    fun setPreviewImage(
        shouldPreview: Boolean,
        url: String
    ) {
        savedStateHandle[shouldPreviewImageKey] = shouldPreview
        savedStateHandle[previewImageUrlKey] = url
    }
}

sealed interface SocialsState {
    data object Loading: SocialsState
    data class Success(val data: SocialsData): SocialsState
}