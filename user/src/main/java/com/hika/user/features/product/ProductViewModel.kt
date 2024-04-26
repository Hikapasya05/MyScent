package com.hika.user.features.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hika.data.data.repository.cart.CartRepository
import com.hika.data.data.repository.favorite.FavoriteRepository
import com.hika.data.data.repository.perfume.PerfumeRepository
import com.hika.data.data.repository.review.ReviewRepository
import com.hika.data.model.Review
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductViewModel(
    private val perfumeRepository: PerfumeRepository,
    private val reviewRepository: ReviewRepository,
    private val cartRepository: CartRepository,
    private val favoriteRepository: FavoriteRepository
): ViewModel() {

    private val _productState = MutableStateFlow(ProductState())
    val productState = _productState.asStateFlow()

    private val _reviews = MutableStateFlow(emptyList<Review>())
    val reviews = _reviews.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite = _isFavorite.asStateFlow()

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

    fun addItem(productId: String, name: String, price: Int, image: String) {
        viewModelScope.launch {
            cartRepository.addItem(
                productId, name, price, image
            )
        }
    }

    fun isFavorite(perfumeId: String) {
        viewModelScope.launch {
            favoriteRepository.isFavorite(perfumeId).onSuccess {
                _isFavorite.value = it
            }
        }
    }

    fun onFavoriteIconPressed(perfumeId: String) {
        viewModelScope.launch {
            val isFavorite = _isFavorite.value
            val result = if (isFavorite) {
                favoriteRepository.deleteFavorite(perfumeId)
            } else {
                favoriteRepository.addFavorite(perfumeId)
            }

            result.onSuccess {
                _isFavorite.value = !isFavorite
            }
        }
    }

}