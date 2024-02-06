package com.hika.myscent.model

import com.google.firebase.Timestamp

data class Review(
    val id: String = "",
    val username: String = "",
    val rating: Double = 0.0,
    val review: String = "",
    val date: Timestamp = Timestamp.now()
)
