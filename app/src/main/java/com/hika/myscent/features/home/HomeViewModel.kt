package com.hika.myscent.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hika.myscent.data.repository.perfume.PerfumeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: PerfumeRepository
) : ViewModel() {

    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()

    fun getPerfumes() {
        viewModelScope.launch {
            _homeState.value = HomeState(isLoading = true)
            repository.getPerfumes().let { result ->
                if (result.isSuccess) {
                    _homeState.value = HomeState(isSuccess = true, successData = result.getOrNull())
                } else {
                    _homeState.value = HomeState(isError = true, errorMessage = result.exceptionOrNull()?.message.orEmpty())
                }
            }
            _homeState.value = _homeState.value.copy(isLoading = false)
        }
    }

}