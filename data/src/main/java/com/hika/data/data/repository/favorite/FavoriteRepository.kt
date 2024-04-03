package com.hika.data.data.repository.favorite

import com.hika.data.model.Perfume

interface FavoriteRepository {
    suspend fun isFavorite(perfumeId: String): Result<Boolean>
    suspend fun addFavorite(perfumeId: String): Result<Unit>
    suspend fun deleteFavorite(perfumeId: String): Result<Unit>
    suspend fun getFavorites(): Result<List<Perfume>>
}