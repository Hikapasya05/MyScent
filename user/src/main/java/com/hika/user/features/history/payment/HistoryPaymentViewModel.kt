package com.hika.user.features.history.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hika.common.util.OrderStatus
import com.hika.data.data.repository.history.HistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HistoryPaymentViewModel(
    private val historyRepository: HistoryRepository
): ViewModel() {

    private val _state = MutableStateFlow(HistoryPaymentState())
    val state = _state.asStateFlow()

    fun getHistoryById(historyId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            historyRepository.getHistoryById(historyId).onSuccess {
                _state.value = _state.value.copy(isLoading = false, isSuccess = true, successData = it)
            }.onFailure {
                _state.value = _state.value.copy(isLoading = false, isError = true, errorMessage = "Failed to get history")
            }
        }
    }

    fun confirmPayment(historyId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            historyRepository.updateHistoryStatus(historyId, OrderStatus.WAIT_FOR_ADMIN_PAYMENT_APPROVAL.name, null).onSuccess {
                _state.value = _state.value.copy(isLoading = false, isSuccess = true)
                onSuccess()
            }.onFailure {
                _state.value = _state.value.copy(isLoading = false, isError = true, errorMessage = "Failed to confirm payment")
            }
        }
    }
}