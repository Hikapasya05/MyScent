package com.hika.data.data.repository.auth

import com.hika.data.model.Role
import com.hika.data.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Role>
    suspend fun register(user: User, password: String): Result<Unit>
    suspend fun logout(): Result<Unit>
}