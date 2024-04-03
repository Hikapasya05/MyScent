package com.hika.data.model

import com.google.firebase.Timestamp

data class Review(
    val id: String = "",
    val username: String? = null,
    val rating: Int = 0,
    val review: String = "",
    val date: Timestamp = Timestamp.now()
)

data class ReviewRequest(
    val uid: String = "",
    val rating: Int = 0,
    val review: String = "",
    val date: Timestamp = Timestamp.now()
)
