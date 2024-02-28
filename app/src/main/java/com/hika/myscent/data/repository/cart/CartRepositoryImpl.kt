package com.hika.myscent.data.repository.cart

import com.google.firebase.auth.FirebaseAuth
import com.hika.myscent.data.local.ScentDao
import com.hika.myscent.model.Cart
import com.hika.myscent.model.CartEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CartRepositoryImpl(
    private val dao: ScentDao,
    private val auth: FirebaseAuth,
): CartRepository {
    override suspend fun addItem(productId: String, name: String, price: Int, image: String) {
        val isExist = dao.isExist(productId) != null
        if (isExist) {
            dao.increaseAmount(productId)
        } else {
            val entity = CartEntity(
                productId,
                name,
                price,
                image,
                1
            )
            dao.insert(entity)
        }
    }

    //"remove" means decrease the amount of the product
    override suspend fun removeItem(perfumeId: String) {
        dao.decreaseAmount(perfumeId)
    }

    //"delete" means remove the product from the cart
    override suspend fun deleteItem(perfumeId: String) {
        dao.deleteItem(perfumeId)
    }

    override suspend fun getCarts(): Flow<List<Cart>> {
        return dao.getCarts().map {
            it.map { entity ->
                Cart(
                    auth.currentUser?.uid.orEmpty(),
                    entity.productId,
                    entity.name,
                    entity.price,
                    entity.amount,
                    entity.image
                )
            }
        }
    }
}