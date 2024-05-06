package com.hika.data.data.repository.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hika.data.data.util.FirestoreCollection.USERS
import com.hika.data.data.util.toUser
import com.hika.data.model.Role
import com.hika.data.model.User
import com.hika.data.util.AuthorizeStatus
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
): UserRepository {
    override suspend fun getAuthorizedUser(): AuthorizeStatus {
        if (auth.currentUser == null) {
            return AuthorizeStatus.GUEST
        }

        getUser().onSuccess {
            return if (it.role == Role.USER.value) AuthorizeStatus.USER else AuthorizeStatus.ADMIN
        }.onFailure {
            return AuthorizeStatus.GUEST
        }

        return AuthorizeStatus.GUEST
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

    override suspend fun updateUser(user: User): Result<Unit> {
        return try {
            firestore.collection(USERS)
                .document(auth.uid.orEmpty())
                .set(user)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}