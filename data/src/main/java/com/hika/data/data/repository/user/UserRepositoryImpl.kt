package com.hika.data.data.repository.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hika.data.data.util.FirestoreCollection.USERS
import com.hika.data.data.util.toUser
import com.hika.data.model.User
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
): UserRepository {
    override suspend fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

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