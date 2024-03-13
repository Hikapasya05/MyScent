package com.hika.myscent.data.repository.favorite

import com.hika.myscent.model.Perfume

interface FavoriteRepository {
    suspend fun isFavorite(perfumeId: String): Result<Boolean>
    suspend fun addFavorite(perfumeId: String): Result<Unit>
    suspend fun deleteFavorite(perfumeId: String): Result<Unit>
    suspend fun getFavorites(): Result<List<Perfume>>
}