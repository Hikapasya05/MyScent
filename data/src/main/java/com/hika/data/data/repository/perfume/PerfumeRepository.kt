package com.hika.data.data.repository.perfume

import com.hika.data.model.HomePerfume
import com.hika.data.model.Perfume

interface PerfumeRepository {
    suspend fun getPerfumes(): Result<List<HomePerfume>>
    suspend fun getPerfumeDetail(id: String): Result<Perfume>
}