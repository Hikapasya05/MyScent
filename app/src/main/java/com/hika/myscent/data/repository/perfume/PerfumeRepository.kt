package com.hika.myscent.data.repository.perfume

import com.hika.myscent.model.Perfume

interface PerfumeRepository {
    suspend fun getPerfumes(): Result<Map<String, List<Perfume>>>
}