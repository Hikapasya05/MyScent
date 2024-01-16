package com.hika.myscent.data.repository

import com.hika.myscent.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun register(user: User, password: String): Result<Unit>
    suspend fun logout(): Result<Unit>
}