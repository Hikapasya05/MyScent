package com.hika.myscent.features.review

import com.hika.myscent.base.BaseState

data class ReviewState(
    override val isLoading: Boolean = false,
    override val isError: Boolean = false,
    override val errorMessage: String = "",
    override val isSuccess: Boolean = false,
    override val successData: Unit? = Unit
): BaseState<Unit>() {
    fun clear() = copy(
        isLoading = false,
        isError = false,
        errorMessage = "",
        isSuccess = false,
        successData = null
    )
}
