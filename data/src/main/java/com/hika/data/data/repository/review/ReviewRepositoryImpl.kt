package com.hika.data.data.repository.review

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hika.data.data.util.FirestoreCollection.PERFUMES
import com.hika.data.data.util.FirestoreCollection.REVIEWS
import com.hika.data.data.util.FirestoreCollection.USERS
import com.hika.data.data.util.toReview
import com.hika.data.model.Review
import com.hika.data.model.ReviewRequest
import kotlinx.coroutines.tasks.await

class ReviewRepositoryImpl(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): ReviewRepository {

    override suspend fun getReviews(perfumeId: String): Result<List<Review>> {
        return try {
            val username = firestore.collection(USERS).document(auth.uid.orEmpty())
                .get().await().getString("username").orEmpty()

            val reviews = firestore.collection(PERFUMES).document(perfumeId).collection(REVIEWS)
                .get().await().documents.map {
                    it.toReview(username)
                }

            Result.success(reviews)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun postReview(perfumeId: String, rating: Int, review: String): Result<Unit> {
        return try {
            val body = ReviewRequest(
                uid = auth.uid.orEmpty(),
                rating = rating,
                review = review,
                date = Timestamp.now()
            )

            firestore.collection(PERFUMES).document(perfumeId).collection(REVIEWS)
                .document().set(body).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}