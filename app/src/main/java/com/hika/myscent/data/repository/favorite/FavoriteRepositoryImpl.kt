package com.hika.myscent.data.repository.favorite

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.getField
import com.hika.myscent.data.util.FirestoreCollection.FAVORITES
import com.hika.myscent.data.util.FirestoreCollection.PERFUMES
import com.hika.myscent.data.util.FirestoreCollection.REVIEWS
import com.hika.myscent.data.util.FirestoreCollection.USERS
import com.hika.myscent.data.util.toPerfume
import com.hika.myscent.model.Perfume
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.tasks.await

class FavoriteRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
): FavoriteRepository {
    override suspend fun isFavorite(perfumeId: String): Result<Boolean> {
        return try {
            val uid = auth.currentUser?.uid.orEmpty()
            val favorite = firestore.collection(USERS)
                .document(uid)
                .collection(FAVORITES)
                .whereEqualTo("perfumeId", perfumeId)
                .get().await().documents.firstOrNull()

            Result.success(favorite != null)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun addFavorite(perfumeId: String): Result<Unit> {
        return try {
            val uid = auth.currentUser?.uid.orEmpty()
            val body = hashMapOf("perfumeId" to perfumeId)

            firestore.collection(USERS)
                .document(uid)
                .collection(FAVORITES)
                .add(body)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun deleteFavorite(perfumeId: String): Result<Unit> {
        return try {
            val uid = auth.currentUser?.uid.orEmpty()
            val favorite = firestore.collection(USERS)
                .document(uid)
                .collection(FAVORITES)
                .whereEqualTo("perfumeId", perfumeId)
                .get().await().documents.firstOrNull()

            favorite?.reference?.delete()?.await()

            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun getFavorites(): Result<List<Perfume>> {
        return try {
            val uid = auth.currentUser?.uid.orEmpty()


            val perfumes = coroutineScope {
                val perfumeIds = firestore.collection(USERS)
                    .document(uid)
                    .collection(FAVORITES)
                    .get().await().documents.map { it.getString("perfumeId").orEmpty() }

                val perfumeJobs = perfumeIds.map {
                    async { firestore.collection(PERFUMES).document(it).get().await() }
                }

                val ratingJobs = perfumeIds.map {
                    async {
                        val reviews = firestore.collection(PERFUMES).document(it).collection(REVIEWS).get().await().documents
                        val ratings = reviews.map { it.getField<Int>("rating") ?: 0}.average()
                        it to ratings
                    }
                }

                val perfumes = perfumeJobs.awaitAll()
                val ratings = ratingJobs.awaitAll()

                perfumes.map {
                    val rating = ratings.first { review -> review.first == it.id }
                    it.toPerfume(if (rating.second.isNaN()) 0.0 else rating.second)
                }
            }

            Result.success(perfumes)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}