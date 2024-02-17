package com.hika.myscent.features.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hika.myscent.data.repository.perfume.PerfumeRepository
import com.hika.myscent.data.repository.review.ReviewRepository
import com.hika.myscent.model.Review
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductViewModel(
    private val perfumeRepository: PerfumeRepository,
    private val reviewRepository: ReviewRepository,
): ViewModel() {

    private val _productState = MutableStateFlow(ProductState())
    val productState = _productState.asStateFlow()

    private val _reviews = MutableStateFlow(emptyList<Review>())
    val reviews = _reviews.asStateFlow()

    fun getPerfumeDetail(id: String) {
        viewModelScope.launch {
            _productState.value = ProductState(isLoading = true)

            perfumeRepository.getPerfumeDetail(id).onSuccess {
                _productState.value = ProductState(isSuccess = true, successData = it)
            }.onFailure {
                _productState.value = ProductState(isError = true, errorMessage = it.message ?: "")
            }

            _productState.value = _productState.value.copy(isLoading = false)
        }
    }

    fun getReviews(perfumeId: String) {
        viewModelScope.launch {
            reviewRepository.getReviews(perfumeId).onSuccess {
                _reviews.value = it
            }
        }
    }

}