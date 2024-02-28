package com.hika.myscent.model

import com.google.firebase.Timestamp

data class HistoryBody(
    val uid: String,
    val date: Timestamp,
    val productToAmount: Map<String, Int>,
    val totalPrice: Int,
    val paymentMethod: String,
    val status: String,
)