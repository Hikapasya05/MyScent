package com.hika.data.data.repository.cart

import com.hika.data.model.Cart
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun addItem(
        productId: String,
        name: String,
        price: Int,
        image: String,
    )
    suspend fun removeItem(perfumeId: String)
    suspend fun deleteItem(perfumeId: String)
    suspend fun getCarts(): Flow<List<Cart>>
}