package com.hika.myscent.data.repository.history

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hika.myscent.data.local.ScentDao
import com.hika.myscent.data.util.FirestoreCollection.HISTORIES
import com.hika.myscent.model.HistoryBody
import kotlinx.coroutines.tasks.await

class HistoryRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val dao: ScentDao,
): HistoryRepository {

    override suspend fun postHistory(body: HistoryBody): Result<Unit> {
        return try {
            firestore.collection(HISTORIES)
                .document()
                .set(body.copy(uid = auth.uid.orEmpty()))
                .await()

            body.productToAmount.forEach { (productId, _) ->
                dao.deleteItem(productId)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}