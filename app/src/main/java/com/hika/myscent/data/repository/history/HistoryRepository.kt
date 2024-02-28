package com.hika.myscent.data.repository.history

import com.hika.myscent.model.HistoryBody

interface HistoryRepository {
    suspend fun postHistory(body: HistoryBody): Result<Unit>
}