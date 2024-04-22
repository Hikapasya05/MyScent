package com.hika.data.model

import com.google.firebase.Timestamp
import java.util.Date

data class History(
    val id: String = "",
    val date: Date = Date(),
    val totalPrice: Int = 0,
    val status: String = "",
    val buyerName: String = "",
    val shippingAddress: String = "",
    val reason: String? = null,
    val productToAmount: HashMap<String, Long> = hashMapOf(),
    val products: List<PerfumeHistory> = emptyList(),
)

data class PerfumeHistory(
    val historyId: String = "",
    val id: String = "",
    val name: String = "",
    val amount: Int = 0,
    val totalPrice: Int = 0,
)

data class HistoryBody(
    val uid: String = "",
    val date: Timestamp = Timestamp.now(),
    val productToAmount: Map<String, Int> = emptyMap(),
    val totalPrice: Int = 0,
    val status: String = "",
    val buyerName: String = "",
    val shippingAddress: String = "",
)