package com.hika.data.model

data class User(
    val email: String,
    val username: String,
    val address: String,
    val phoneNumber: String,
    val role: String
)

enum class Role(val value: String) {
    ADMIN("admin"),
    USER("user")
}
