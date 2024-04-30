package com.hika.data.data.repository.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hika.data.data.util.FirestoreCollection
import com.hika.data.model.Role
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
): AuthRepository {
    override suspend fun login(email: String, password: String): Result<Role> {
        return try {
            val userInformation = auth.signInWithEmailAndPassword(email, password).await()
            val userRole = firestore.collection(FirestoreCollection.USERS)
                .document(userInformation.user?.uid.toString())
                .get()
                .await()
                .getString("role")

            if (userRole == Role.ADMIN.value) {
                Result.success(Role.ADMIN)
            } else {
                Result.success(Role.USER)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(user: com.hika.data.model.User, password: String): Result<Unit> {
        return try {
            auth.createUserWithEmailAndPassword(user.email, password).await()
            firestore.collection(FirestoreCollection.USERS).document(auth.uid.orEmpty()).set(user).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            auth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}