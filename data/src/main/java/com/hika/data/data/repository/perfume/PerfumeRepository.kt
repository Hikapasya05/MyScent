package com.hika.data.data.repository.perfume

import android.net.Uri
import com.hika.data.model.HomePerfume
import com.hika.data.model.Perfume
import com.hika.data.model.PerfumeBody

interface PerfumeRepository {
    suspend fun addPerfume(body: PerfumeBody, uri: Uri): Result<Unit>
    suspend fun updatePerfume(id: String, body: PerfumeBody, uri: Uri?, baseUrl: String): Result<Unit>
    suspend fun getPerfumes(): Result<List<HomePerfume>>
    suspend fun getPerfumeDetail(id: String): Result<Perfume>
    suspend fun deletePerfume(id: String): Result<Unit>
}