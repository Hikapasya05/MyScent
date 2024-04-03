package com.hika.data.data.repository.perfume

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.getField
import com.hika.data.data.util.FirestoreCollection.PERFUMES
import com.hika.data.data.util.FirestoreCollection.REVIEWS
import com.hika.data.data.util.FirestoreCollection.USERS
import com.hika.data.data.util.FirestoreField.RATING
import com.hika.data.data.util.toPerfume
import com.hika.data.data.util.toReview
import com.hika.data.model.HomePerfume
import com.hika.data.model.Perfume
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await

class PerfumeRepositoryImpl(
    private val firestore: FirebaseFirestore
): PerfumeRepository {
    override suspend fun getPerfumes(): Result<List<HomePerfume>> {
        return try {
            coroutineScope {
                val perfumesDeferred = async(Dispatchers.IO) {
                    firestore.collection(PERFUMES).get().await().documents.map {
                        it.toPerfume()
                    }
                }
                val perfumes = perfumesDeferred.await()

                val perfumesWithReviewsDeferred = perfumes.map {
                    async {
                        val ratings = firestore.collection(PERFUMES).document(it.id).collection(REVIEWS)
                            .get().await().documents.map { it.getField<Int>(RATING) ?: 0 }.average()

                        it.copy(rating = if (ratings.isNaN()) 0.0 else ratings)
                    }
                }
                val perfumeWithReviews = perfumesWithReviewsDeferred.awaitAll()

                val mappedPerfumes = perfumeWithReviews.groupBy { it.category }
                val homePerfume = mappedPerfumes.map { (category, perfumes) ->
                    HomePerfume(category, perfumes)
                }.sortedBy {
                    when (it.categoryName) {
                        "Men" -> 0
                        "Women" -> 1
                        "Unisex" -> 2
                        else -> 3
                    }
                }

                Result.success(homePerfume)
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun getPerfumeDetail(id: String): Result<Perfume> {
        return try {
            val perfume = firestore.collection(PERFUMES)
                .document(id)
                .get().await()
                .let { perfumeSnapshot ->

                    val reviews = perfumeSnapshot.reference.collection(REVIEWS)
                        .get().await().documents.map { reviewSnapshot ->

                            val username = firestore.collection(USERS)
                                .document(reviewSnapshot.getString("uid").orEmpty())
                                .get().await().let { it.getString("username").orEmpty() }

                            reviewSnapshot.toReview(username)
                        }

                    val averageRating = reviews.map { it.rating }.average()

                    perfumeSnapshot.toPerfume(
                        if (averageRating.isNaN()) 0.0
                        else averageRating
                    )
                }

            Result.success(perfume)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}