package com.hika.data.data.repository.user

import com.hika.data.model.User
import com.hika.data.util.AuthorizeStatus

interface UserRepository {

    suspend fun getAuthorizedUser(): AuthorizeStatus
    suspend fun getUser(): Result<User>
}