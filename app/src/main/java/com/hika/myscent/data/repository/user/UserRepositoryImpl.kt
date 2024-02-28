package com.hika.myscent.data.repository.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hika.myscent.data.util.FirestoreCollection.USERS
import com.hika.myscent.data.util.toUser
import com.hika.myscent.model.User
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
): UserRepository {
    override suspend fun getUser(): Result<User> {
        return try {
            firestore.collection(USERS)
                .document(auth.uid.orEmpty())
                .get()
                .await()
                .toUser()
                .let { Result.success(it) }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}