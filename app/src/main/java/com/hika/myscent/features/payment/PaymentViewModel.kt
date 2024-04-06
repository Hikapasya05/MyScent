package com.hika.myscent.features.payment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.hika.common.util.OrderStatus
import com.hika.data.data.repository.history.HistoryRepository
import com.hika.data.data.repository.user.UserRepository
import com.hika.data.model.Cart
import com.hika.data.model.PaymentMethod
import com.hika.data.model.User
import com.hika.myscent.R
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

    private val _paymentMethod = MutableLiveData<PaymentMethod?>()
    val paymentMethod = _paymentMethod

    fun getUser() {
        viewModelScope.launch {
            userRepository.getUser().onSuccess {
                _user.value = it
            }
        }
    }

    fun setCheckedOutCarts(checkedOutCarts: List<com.hika.data.model.Cart>) {
        _shippingCost.value = listOf(100000, 150000, 200000, 250000).random()
        _admin.value = listOf(10000, 15000, 20000, 25000).random()
        _carts.value = checkedOutCarts

        _totalPrice.value = checkedOutCarts.sumOf { it.price * it.amount } + (_shippingCost.value ?: 0) + (_admin.value ?: 0)
    }

    fun setPaymentMethod(paymentMethod: PaymentMethod?) {
        _paymentMethod.value = paymentMethod
    }

    fun getTotalPrice(): Int {
        return _carts.value?.sumOf { it.price } ?: 0
    }

    fun getPaymentMethods() = listOf(
        PaymentMethod(2, "BNI", R.drawable.ic_bni),
        PaymentMethod(3, "Bank Jago", R.drawable.ic_jago),
        PaymentMethod(4, "BSI", R.drawable.ic_bsi),
        PaymentMethod(5, "BCA", R.drawable.ic_bca),
        PaymentMethod(6, "Bank Mandiri", R.drawable.ic_mandiri),
        PaymentMethod(7, "Bank Mega", R.drawable.ic_mega),
        PaymentMethod(8, "BRI", R.drawable.ic_bri),
        PaymentMethod(9, "BTN", R.drawable.ic_btn),
    )

    fun postHistory() {
        viewModelScope.launch {
            _uiState.value = PaymentState(isLoading = true)
            val body = com.hika.data.model.HistoryBody(
                "",
                Timestamp.now(),
                _carts.value?.associate { it.productId to it.amount } ?: emptyMap(),
                _totalPrice.value ?: 0,
                _paymentMethod.value?.name.orEmpty(),
                OrderStatus.PROCESSING.name
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