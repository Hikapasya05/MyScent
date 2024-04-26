package com.hika.user.features.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hika.data.data.repository.favorite.FavoriteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _favoriteState = MutableStateFlow(FavoriteState())
    val favoriteState = _favoriteState.asStateFlow()

    fun getFavorite() {
        _favoriteState.value = FavoriteState(isLoading = true)
        viewModelScope.launch {
            favoriteRepository.getFavorites().onSuccess {
                _favoriteState.value = FavoriteState(isSuccess = true, successData = it)
            }.onFailure {
                _favoriteState.value = FavoriteState(isError = true, errorMessage = it.message.orEmpty())
            }
        }
    }

    fun deleteFavorite(perfumeId: String) {
        viewModelScope.launch {
            _favoriteState.value = FavoriteState(isLoading = true)
            favoriteRepository.deleteFavorite(perfumeId).onSuccess {
                getFavorite()
            }.onFailure {
                _favoriteState.value = FavoriteState(isError = true, errorMessage = it.message.orEmpty())
            }
        }
    }
}