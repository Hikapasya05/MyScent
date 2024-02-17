package com.hika.myscent.features.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hika.myscent.data.repository.review.ReviewRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReviewViewModel(
    private val repository: ReviewRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ReviewState())
    val state = _state.asStateFlow()

    private val _review = MutableStateFlow(0.0)
    val review = _review.asStateFlow()

    fun addReview(rating: Int, review: String, perfumeId: String) {
        _state.value.clear()

        if (rating == 0) {
            _state.value = ReviewState(isError = true, errorMessage = "Rating must be more than 0")
            return
        }

        if (review.isEmpty()) {
            _state.value = ReviewState(isError = true, errorMessage = "Review must not be empty")
            return
        }

        viewModelScope.launch {
            _state.value = ReviewState(isLoading = true)
            repository.postReview(perfumeId, rating, review)
                .onSuccess {
                    _state.value = ReviewState(isLoading = false, isSuccess = true)
                }
                .onFailure {
                    _state.value = ReviewState(isLoading = false, errorMessage = it.message.orEmpty())
                }
        }
    }

}