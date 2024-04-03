package com.hika.data.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hika.data.model.CartEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScentDao {

    @Insert
    suspend fun insert(cartEntity: CartEntity)

    @Query("SELECT * FROM cart_table")
    fun getCarts(): Flow<List<CartEntity>>

    @Query("UPDATE cart_table SET amount = amount + 1 WHERE productId = :productId")
    suspend fun increaseAmount(productId: String)

    @Query("UPDATE cart_table SET amount = amount - 1 WHERE productId = :productId")
    suspend fun decreaseAmount(productId: String)

    @Query("DELETE FROM cart_table WHERE productId = :productId")
    suspend fun deleteItem(productId: String)

    @Query("SELECT * FROM cart_table WHERE productId = :productId")
    suspend fun isExist(productId: String): CartEntity?
}