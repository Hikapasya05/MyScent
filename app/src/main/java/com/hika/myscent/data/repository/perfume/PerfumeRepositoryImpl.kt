package com.hika.myscent.data.repository.perfume

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.getField
import com.hika.myscent.data.util.FirestoreCollection.PERFUMES
import com.hika.myscent.data.util.FirestoreCollection.REVIEWS
import com.hika.myscent.data.util.FirestoreField.RATING
import com.hika.myscent.data.util.toPerfume
import com.hika.myscent.model.Perfume
import kotlinx.coroutines.tasks.await

class PerfumeRepositoryImpl(
    private val firestore: FirebaseFirestore
): PerfumeRepository {
    override suspend fun getPerfumes(): Result<Map<String, List<Perfume>>> {
        return try {

            val perfumes = firestore.collection(PERFUMES)
                .get().await().documents.map {
                    val reviewDocuments = it.reference.collection(REVIEWS)
                        .get().await().documents
                    val ratings = reviewDocuments.map { it.getField<Int>(RATING) ?: 0 }.average()

                    it.toPerfume(
                        if (ratings.isNaN()) 0.0
                        else ratings
                    )
                }

            val mappedPerfumes = perfumes.groupBy { it.category }

            Result.success(mappedPerfumes)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}