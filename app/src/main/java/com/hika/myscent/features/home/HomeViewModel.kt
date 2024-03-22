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

    private val _query = MutableStateFlow("")

    fun getPerfumes() {
        viewModelScope.launch {
            _homeState.value = _homeState.value.copy(isLoading = true)
            repository.getPerfumes().let { result ->
                if (result.isSuccess) {
                    _homeState.value = _homeState.value.copy(isError = false, isSuccess = true, homePerfumes = result.getOrNull().orEmpty())
                } else {
                    _homeState.value = _homeState.value.copy(isError = true, isSuccess = false, errorMessage = result.exceptionOrNull()?.message.orEmpty())
                }
            }
            _homeState.value = _homeState.value.copy(isLoading = false)
        }
    }

    private fun searchPerfumes() {
        viewModelScope.launch {
            _homeState.value = _homeState.value.copy(isLoading = true)
            val allPerfumes = _homeState.value.homePerfumes.map { it.perfumes }.flatten()
            val result = allPerfumes.filter { it.name.contains(_query.value, ignoreCase = true) }
            _homeState.value = _homeState.value.copy(isError = false, isSuccess = true, searchedPerfumes = result)
            _homeState.value = _homeState.value.copy(isLoading = false)
        }
    }

    fun setQuery(query: String) {
        if (query.isEmpty()) {
            _homeState.value = _homeState.value.copy(listType = ListType.HOME)
            val isDataFetched = _homeState.value.homePerfumes.isNotEmpty()
            if (!isDataFetched) getPerfumes()
        } else {
            _homeState.value = _homeState.value.copy(listType = ListType.SEARCH)
            _query.value = query
            searchPerfumes()
        }
    }

}