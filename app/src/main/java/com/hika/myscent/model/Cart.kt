package com.hika.myscent.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cart(
    val uid: String,
    val productId: String,
    val name: String,
    val price: Int,
    val amount: Int,
    val image: String
): Parcelable {
    fun increaseAmount() = copy(amount = amount + 1)
    fun decreaseAmount() = copy(amount = amount - 1)

    fun emptyObject() = Cart("", "", "", 0, 0, "")
}

@Entity(tableName = "cart_table")
data class CartEntity(
    @PrimaryKey
    val productId: String,
    val name: String,
    val price: Int,
    val image: String,
    val amount: Int
)
