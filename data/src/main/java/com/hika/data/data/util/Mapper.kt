package com.hika.data.data.util

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.getField
import com.hika.data.model.History
import com.hika.data.model.Perfume
import com.hika.data.model.PerfumeHistory
import com.hika.data.model.Review
import com.hika.data.model.User

fun DocumentSnapshot.toPerfume(
    rating: Double = 0.0
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
    getField<Int>("rating") ?: 0,
    getString("review").orEmpty(),
    getTimestamp("date") ?: Timestamp.now()
)

fun DocumentSnapshot.toUser() = User(
    getString("email").orEmpty(),
    getString("username").orEmpty(),
    getString("address").orEmpty(),
    getString("phoneNumber").orEmpty(),
    getString("role").orEmpty()
)

fun DocumentSnapshot.toHistory(
    perfumeHistories: List<PerfumeHistory> = emptyList()
) = History(
    id,
    getTimestamp("date") ?: Timestamp.now(),
    getField<Int>("totalPrice") ?: 0,
    getString("status").orEmpty(),
    getString("buyerName").orEmpty(),
    getString("shippingAddress").orEmpty(),
    get("productToAmount") as? HashMap<String, Long> ?: hashMapOf(),
    perfumeHistories
)

fun DocumentSnapshot.toPerfumeHistory(
    historyId: String,
    amount: Int
) = PerfumeHistory(
    historyId,
    id,
    getString("name").orEmpty(),
    amount,
    (getField<Int>("price") ?: 0) * amount,
)