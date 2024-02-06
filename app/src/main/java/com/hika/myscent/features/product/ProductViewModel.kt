package com.hika.myscent.features.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hika.myscent.data.repository.perfume.PerfumeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductViewModel(
    private val repository: PerfumeRepository
): ViewModel() {

    private val _productState = MutableStateFlow(ProductState())
    val productState = _productState.asStateFlow()

    fun getPerfumeDetail(id: String) {
        viewModelScope.launch {
            _productState.value = ProductState(isLoading = true)

            repository.getPerfumeDetail(id).onSuccess {
                _productState.value = ProductState(isSuccess = true, successData = it)
            }.onFailure {
                _productState.value = ProductState(isError = true, errorMessage = it.message ?: "")
            }

            _productState.value = _productState.value.copy(isLoading = false)
        }
    }

}