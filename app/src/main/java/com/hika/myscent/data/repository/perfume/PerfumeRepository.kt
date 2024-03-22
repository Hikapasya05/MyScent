package com.hika.myscent.data.repository.perfume

import com.hika.myscent.model.HomePerfume
import com.hika.myscent.model.Perfume

interface PerfumeRepository {
    suspend fun getPerfumes(): Result<List<HomePerfume>>
    suspend fun getPerfumeDetail(id: String): Result<Perfume>
}