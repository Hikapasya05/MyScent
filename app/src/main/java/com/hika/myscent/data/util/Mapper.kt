package com.hika.myscent.data.util

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.getField
import com.hika.myscent.model.Perfume
import com.hika.myscent.model.Review

fun DocumentSnapshot.toPerfume(
    rating: Double
) = Perfume(
    id,
    getString("name").orEmpty(),
    getString("image").orEmpty(),
    getString("category").orEmpty(),
    getString("description").orEmpty(),
    getField<Int>("strength") ?: 0,
    getField<Int>("price") ?: 0,
    rating = rating
)

fun DocumentSnapshot.toReview(
    username: String
) = Review(
    id,
    username,
    getField<Int>("rating")?.toDouble() ?: 0.0,
    getString("review").orEmpty(),
    getTimestamp("date") ?: Timestamp.now()
)