package com.hika.data.data.repository.review

import com.hika.data.model.Review

interface ReviewRepository {

    suspend fun getReviews(perfumeId: String): Result<List<Review>>
    suspend fun postReview(perfumeId: String, rating: Int, review: String): Result<Unit>
}