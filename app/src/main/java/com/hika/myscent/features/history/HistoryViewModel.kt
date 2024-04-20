package com.hika.myscent.features.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hika.common.util.OrderStatus
import com.hika.data.data.repository.history.HistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val historyRepository: HistoryRepository
): ViewModel() {

    private val _state = MutableStateFlow(HistoryState())
    val state = _state.asStateFlow()

    fun getHistories() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            historyRepository.getHistories().onSuccess {
                _state.value = _state.value.copy(isSuccess = true, successData = it)
            }.onFailure {
                _state.value = _state.value.copy(isError = true, errorMessage = it.message.orEmpty())
            }
            _state.value = _state.value.copy(isLoading = false)
        }
    }

    fun rejectHistory(historyId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            historyRepository.updateHistoryStatus(historyId, OrderStatus.REJECTED_BY_USER.name).onSuccess {
                getHistories()
            }.onFailure {
                _state.value = _state.value.copy(isError = true, errorMessage = it.message.orEmpty())
            }
        }
        _state.value = _state.value.copy(isLoading = false)
    }
}