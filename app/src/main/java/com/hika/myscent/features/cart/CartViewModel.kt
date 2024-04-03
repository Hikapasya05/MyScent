package com.hika.myscent.features.cart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hika.data.data.repository.cart.CartRepository
import com.hika.data.model.Cart
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _carts = MutableLiveData(mutableListOf<Pair<Cart, Boolean>>())
    val carts = _carts

    fun getCarts() = viewModelScope.launch {
        cartRepository.getCarts().collect {
            _carts.postValue(
                if (_carts.value.isNullOrEmpty()) {
                    it.map { it to false }.toMutableList()
                } else {
                    it.map { cart ->
                        val index = _carts.value?.indexOfFirst { it.first.productId == cart.productId }
                        if (index != -1) {
                            _carts.value?.get(index ?: -1)?.second?.let { isChecked ->
                                cart to isChecked
                            } ?: (cart to false)
                        } else {
                            cart to false
                        }
                    }.toMutableList()
                }
            )
        }
    }

    fun deleteCart(perfumeId: String) = viewModelScope.launch {
        cartRepository.deleteItem(perfumeId)
    }

    fun removeItem(perfumeId: String) = viewModelScope.launch {
        cartRepository.removeItem(perfumeId)
    }

    fun addItem(
        productId: String, name: String, price: Int, image: String
    ) = viewModelScope.launch {
        cartRepository.addItem(
            productId, name, price, image
        )
    }

    fun onCheckedChange(productId: String, isChecked: Boolean) {
        val index = _carts.value?.indexOfFirst { it.first.productId == productId }
        if (index != -1) {
            _carts.value?.get(index ?: -1)?.let {
                val cart = it.first
                _carts.value?.set(index ?: -1, cart to isChecked)
            }
            _carts.postValue(_carts.value)
        }
    }

    fun onSelectAll(isChecked: Boolean) {
        _carts.value?.forEachIndexed { index, pair ->
            _carts.value?.set(index, pair.first to isChecked)
        }
        _carts.postValue(_carts.value)
    }

}