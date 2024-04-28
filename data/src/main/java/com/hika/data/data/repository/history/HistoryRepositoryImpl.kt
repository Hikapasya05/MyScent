package com.hika.data.data.repository.history

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hika.data.data.local.ScentDao
import com.hika.data.data.util.FirestoreCollection.HISTORIES
import com.hika.data.data.util.FirestoreCollection.PERFUMES
import com.hika.data.data.util.FirestoreCollection.USERS
import com.hika.data.data.util.toHistory
import com.hika.data.data.util.toPerfumeHistory
import com.hika.data.data.util.toUser
import com.hika.data.model.History
import com.hika.data.model.HistoryBody
import com.hika.data.model.Role
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await

class HistoryRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val dao: ScentDao,
): HistoryRepository {

    override suspend fun postHistory(body: HistoryBody): Result<Unit> {
        return try {

            val userIdentity = firestore.collection(USERS)
                .document(auth.uid.orEmpty()).get().await().toUser()

            firestore.collection(HISTORIES)
                .document()
                .set(body.copy(
                    uid = auth.uid.orEmpty(),
                    date = Timestamp.now().toDate(),
                    buyerName = userIdentity.username,
                    shippingAddress = userIdentity.address
                ))
                .await()

            body.productToAmount.forEach { (productId, _) ->
                dao.deleteItem(productId)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getHistories(): Result<List<History>> {
        return try {
            coroutineScope {
                val userRole = firestore.collection(USERS)
                    .document(auth.uid.orEmpty())
                    .get()
                    .await()
                    .getString("role")

                val histories = if (userRole == Role.USER.value) {
                    firestore.collection(HISTORIES).whereEqualTo("uid", auth.uid.orEmpty())
                        .get().await().map { it.toHistory() }
                } else {
                    firestore.collection(HISTORIES).get().await().map { it.toHistory() }
                }

                val perfumeHistories = histories.map {
                    val perfumes = it.productToAmount.map { map ->
                       async { firestore.collection(PERFUMES).document(map.key).get().await() }
                    }
                    async { perfumes.awaitAll().map { perfume ->
                        perfume.toPerfumeHistory(it.id, it.productToAmount[perfume.id]?.toInt() ?: 0)
                    } }
                }.awaitAll().flatten()

                val result = histories.map { history ->
                    history.copy(products = perfumeHistories.filter { it.historyId == history.id })
                }.sortedByDescending { it.date }

                Result.success(result)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getHistoryById(historyId: String): Result<History> {
        return try {
            val history = firestore.collection(HISTORIES)
                .document(historyId)
                .get()
                .await()
                .toHistory()

            val perfumes = firestore.collection(PERFUMES)
                .get()
                .await()
                .map { it.toPerfumeHistory(historyId, history.productToAmount[it.id]?.toInt() ?: 0) }

            Result.success(history.copy(products = perfumes))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateHistoryStatus(
        historyId: String,
        updatedOrderStatus: String,
        reason: String?
    ): Result<Unit> {
        return try {
            firestore.collection(HISTORIES)
                .document(historyId)
                .update(
                    if (reason == null)
                        mapOf("status" to updatedOrderStatus)
                    else
                        mapOf(
                            "status" to updatedOrderStatus,
                            "reason" to reason
                        )
                )
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}