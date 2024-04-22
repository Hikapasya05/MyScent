package com.hika.admin.features.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hika.common.util.OrderStatus
import com.hika.data.data.repository.history.HistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderAdminViewModel(
    private val orderRepository: HistoryRepository
) : ViewModel() {

    private val _state = MutableStateFlow(OrderAdminState())
    val state = _state.asStateFlow()

    fun getOrders() {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            orderRepository.getHistories().onSuccess {
                _state.value = _state.value.copy(isSuccess = true, successData = it)
            }.onFailure {
                _state.value = _state.value.copy(isError = true, errorMessage = it.message.orEmpty())
            }
            _state.value = _state.value.copy(isLoading = false)
        }
    }

    fun confirmOrder(orderId: String, status: String) {
        _state.value = _state.value.copy(isLoading = true)
        val updatedOrderStatus = when (status) {
            OrderStatus.WAIT_FOR_ADMIN_CONFIRMATION.name -> OrderStatus.WAIT_FOR_USER_PAYMENT
            OrderStatus.WAIT_FOR_ADMIN_PAYMENT_APPROVAL.name -> OrderStatus.SHIPPING
            else -> OrderStatus.REJECTED_BY_ADMIN
        }
        viewModelScope.launch {
            orderRepository.updateHistoryStatus(orderId, updatedOrderStatus.name, null).onSuccess {
                getOrders()
            }.onFailure {
                _state.value = _state.value.copy(isError = true, errorMessage = it.message.orEmpty())
            }
            _state.value = _state.value.copy(isLoading = false)
        }
    }

    fun rejectOrder(orderId: String, reason: String) {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            orderRepository.updateHistoryStatus(orderId, OrderStatus.REJECTED_BY_ADMIN.name, reason).onSuccess {
                getOrders()
            }.onFailure {
                _state.value = _state.value.copy(isError = true, errorMessage = it.message.orEmpty())
            }
            _state.value = _state.value.copy(isLoading = false)
        }
    }

}