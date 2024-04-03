package com.hika.data.data.repository.user

import com.hika.data.model.User

interface UserRepository {

    suspend fun isUserLoggedIn(): Boolean
    suspend fun getUser(): Result<User>
}