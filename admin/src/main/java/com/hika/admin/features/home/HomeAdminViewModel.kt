package com.hika.admin.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hika.data.data.repository.perfume.PerfumeRepository
import com.hika.data.model.Perfume
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeAdminViewModel(
    private val perfumeRepository: PerfumeRepository
) : ViewModel() {

    private val _homeState = MutableStateFlow(HomeAdminState())
    val homeState = _homeState.asStateFlow()

    private val _rawPerfumes = MutableStateFlow(emptyList<Perfume>())

    fun getPerfumes() {
        viewModelScope.launch {
            _homeState.value = _homeState.value.copy(isLoading = true)
            perfumeRepository.getPerfumes().let { result ->
                if (result.isSuccess) {
                    _rawPerfumes.value = result.getOrNull()?.map { it.perfumes }?.flatten().orEmpty()
                    _homeState.value = _homeState.value.copy(isError = false, isSuccess = true, successData = _rawPerfumes.value)
                } else {
                    _homeState.value = _homeState.value.copy(isError = true, isSuccess = false, errorMessage = result.exceptionOrNull()?.message.orEmpty())
                }
            }
            _homeState.value = _homeState.value.copy(isLoading = false)
        }
    }

    fun searchPerfumes(
        query: String,
    ) {
        viewModelScope.launch {
            if (query.isEmpty()) {
                _homeState.value = _homeState.value.copy(isError = false, isSuccess = true, successData = _rawPerfumes.value)
                return@launch
            } else {
                val result = _homeState.value.successData?.filter { it.name.contains(query, ignoreCase = true) }
                _homeState.value = _homeState.value.copy(isError = false, isSuccess = true, successData = result)
            }
        }
    }
}