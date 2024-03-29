package com.hika.myscent.data.repository.user

import com.hika.myscent.model.User

interface UserRepository {

    suspend fun getUser(): Result<User>
}