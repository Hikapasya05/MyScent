package com.hika.myscent.model

import androidx.room.Entity

data class User(
    val email: String,
    val username: String,
    val address: String,
    val phoneNumber: String,
)
