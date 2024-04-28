package com.hika.user.features.payment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hika.common.util.OrderStatus
import com.hika.data.data.repository.history.HistoryRepository
import com.hika.data.data.repository.user.UserRepository
import com.hika.data.model.Cart
import com.hika.data.model.HistoryBody
import com.hika.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PaymentViewModel(
    private val userRepository: UserRepository,
    private val historyRepository: HistoryRepository,
): ViewModel() {

    private val _uiState = MutableStateFlow(PaymentState())
    val uiState = _uiState.asStateFlow()

    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    private val _carts = MutableLiveData<List<Cart>>()
    val carts = _carts

    private val _totalPrice = MutableLiveData<Int>()
    val totalPrice = _totalPrice

    private val _shippingCost = MutableLiveData<Int>()
    val shippingCost = _shippingCost

    private val _admin = MutableLiveData<Int>()
    val admin = _admin

    private val _uniqueCode = MutableLiveData<Int>()
    val uniqueCode = _uniqueCode

    fun getUser() {
        viewModelScope.launch {
            userRepository.getUser().onSuccess {
                _user.value = it
            }
        }
    }

    fun setCheckedOutCarts(checkedOutCarts: List<Cart>) {
        _shippingCost.value = listOf(100000, 150000, 200000, 250000).random()
        _admin.value = listOf(10000, 15000, 20000, 25000).random()
        _carts.value = checkedOutCarts
        _uniqueCode.value = (1..999).random()

        _totalPrice.value = checkedOutCarts.sumOf { it.price * it.amount } + (_shippingCost.value ?: 0) + (_admin.value ?: 0) + (_uniqueCode.value ?: 0)
    }

    fun getTotalPrice(): Int {
        return _carts.value?.sumOf { it.price } ?: 0
    }

    fun postHistory() {
        viewModelScope.launch {
            _uiState.value = PaymentState(isLoading = true)
            val body = HistoryBody(
                productToAmount = _carts.value?.associate { it.productId to it.amount } ?: emptyMap(),
                totalPrice = _totalPrice.value ?: 0,
                status = OrderStatus.WAIT_FOR_ADMIN_CONFIRMATION.name,
            )
            historyRepository.postHistory(body).onSuccess {
                _uiState.value = PaymentState(isLoading = false, isSuccess = true)
            }.onFailure {
                _uiState.value = PaymentState(isLoading = false, isError = true, errorMessage = it.message.orEmpty())
            }

            _uiState.value.clear()
        }
    }
}