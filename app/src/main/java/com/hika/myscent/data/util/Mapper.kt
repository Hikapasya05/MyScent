package com.hika.myscent.data.util

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.getField
import com.hika.myscent.model.Perfume

fun DocumentSnapshot.toPerfume(rating: Double) = Perfume(
    id,
    getString("name").orEmpty(),
    getString("image").orEmpty(),
    getString("category").orEmpty(),
    getString("description").orEmpty(),
    getField<Int>("strong") ?: 0,
    getField<Int>("price") ?: 0,
    rating = rating
)